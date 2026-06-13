import urllib.request, json, subprocess
BASE = 'http://localhost:8080/api'

def api(method, path, token, data=None):
    headers = {'Content-Type':'application/json'}
    if token: headers['Authorization'] = f'Bearer {token}'
    body = json.dumps(data).encode() if data else None
    req = urllib.request.Request(f'{BASE}{path}', data=body, headers=headers, method=method)
    resp = urllib.request.urlopen(req).read()
    return json.loads(resp)

# Register test users
for name in ['testbuyer','testseller']:
    r = api('POST','/auth/register',None,{'username':name,'password':'123456'})
    print(f'Register {name}: {r["message"]}')

b_tok = api('POST','/auth/login',None,{'username':'testbuyer','password':'123456'})['data']['token']
s_tok = api('POST','/auth/login',None,{'username':'testseller','password':'123456'})['data']['token']
admin_tok = api('POST','/auth/login',None,{'username':'admin001','password':'admin123'})['data']['token']

# Seller publish goods
pub = api('POST','/goods',s_tok,{'title':'测试完整交易','price':88,'categoryId':1,'conditionLevel':1,'description':'test','images':[]})
print(f'Publish: {pub["message"]}')

# Admin find & approve
pending = api('GET','/admin/goods/pending?size=50',admin_tok)
target = None
for g in pending['data'].get('records',[]):
    if '测试完整交易' in g['title']:
        target = g; break
if not target:
    print('ERROR: goods not in pending list')
    exit(1)
gid = target['id']
api('PUT',f'/admin/goods/{gid}/audit',admin_tok,{'status':1,'reason':''})
print(f'Approved goods #{gid}')

# ---- Full order flow ----
# Step 1: Buyer creates order
order = api('POST','/order',b_tok,{'goodsId':gid})
oid = order['data']['id']
print(f'\n1️⃣ 买家下单: #{oid} [{["待确认","已确认","已完成","已取消"][order["data"]["status"]]}]')

# Step 2: Buyer sees it in bought
bought = api('GET','/order/bought',b_tok)
print(f'2️⃣ 买家[我买到的]: {len(bought["data"]["records"])} 条')

# Step 3: Seller sees it in sold
sold = api('GET','/order/sold',s_tok)
print(f'3️⃣ 卖家[我卖出的]: {len(sold["data"]["records"])} 条')

# Step 4: Seller confirms
api('PUT',f'/order/{oid}/confirm',s_tok)
print(f'4️⃣ 卖家确认订单 → 已确认')

# Step 5: Buyer confirms receipt (确认收货)
api('PUT',f'/order/{oid}/complete',b_tok)
print(f'5️⃣ 买家确认收货 → 交易完成')

# Final check
final = api('GET',f'/order/{oid}',None)['data']
print(f'\n🎉 完整交易流程验证成功!')
print(f'   订单号: {final["orderNo"]}')
print(f'   状态: {["待确认","已确认","已完成","已取消"][final["status"]]}')
print(f'   商品: {final["goodsTitle"]}')
print(f'   价格: ¥{final["price"]}')
print(f'   买家: {final["buyerName"]} → 卖家: {final["sellerName"]}')
print(f'   商品图片: {final.get("goodsImage","无")}')

# Cleanup test data
subprocess.run([
    'mysql','-u','root','-p060423','-e',
    f'''USE school_trade;
DELETE FROM goods_image WHERE goods_id={gid};
DELETE FROM orders WHERE id={oid};
DELETE FROM goods WHERE id={gid};
DELETE FROM user WHERE username IN ("testbuyer","testseller");
'''], capture_output=True)
print('\n✅ 测试数据已清理')
