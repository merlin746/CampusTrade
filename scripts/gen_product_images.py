#!/usr/bin/env python3
"""
为每件商品生成简单但可辨识的商品插图
每个商品画一个简笔画风格的图标
"""
import os, math
from PIL import Image, ImageDraw, ImageFont, ImageFilter, ImageEnhance

UPLOAD_DIR = './uploads'

# 颜色
WHITE = (255,255,255)
BLACK = (40,40,40)
GRAY  = (150,150,150)
LGRAY = (230,230,230)

FONTS = {}
for name in ['C:/Windows/Fonts/msyhbd.ttf', 'C:/Windows/Fonts/msyh.ttc',
             'C:/Windows/Fonts/arial.ttf', 'C:/Windows/Fonts/segoeui.ttf']:
    try:
        FONTS['title'] = ImageFont.truetype(name, 20)
        break
    except: pass
try:
    FONTS['price'] = ImageFont.truetype('C:/Windows/Fonts/msyhbd.ttf', 28)
except: FONTS['price'] = FONTS['title']

def rounded_rect(draw, xy, r, fill=None, outline=None, width=1):
    x1,y1,x2,y2 = xy
    draw.rounded_rectangle(xy, r, fill=fill, outline=outline, width=width)

def draw_book(draw, x, y, color=(99,102,241), w=120, h=160):
    """画一本书"""
    # 书脊
    draw.rounded_rectangle([x,y, x+16,h+y], 4, fill=darken(color), outline=darken(color,0.3))
    # 封面
    draw.rounded_rectangle([x+10,y+4, x+w, h+y], 6, fill=color, outline=darken(color,0.3), width=2)
    # 内页线条
    for i in range(3):
        ly = y+30+i*25
        draw.line([x+28, ly, x+w-16, ly], fill=(245,245,245), width=3)

def draw_phone(draw, x, y, w=100, h=170):
    """画一个手机/平板"""
    draw.rounded_rectangle([x,y, x+w, y+h], 18, fill=(30,30,35), outline=(60,60,65), width=2)
    # 屏幕
    draw.rounded_rectangle([x+8, y+14, x+w-8, y+h-60], 8, fill=(230,240,255))
    # 图标
    for r in range(3):
        cx = x + 30 + r*20
        draw.rounded_rectangle([cx-10, y+30, cx+10, y+50], 6, fill=(99,102,241))
    # Home按钮
    draw.ellipse([x+w//2-10, y+h-40, x+w//2+10, y+h-20], fill=(80,80,85))

def draw_laptop(draw, x, y):
    """笔记本电脑"""
    draw.rounded_rectangle([x,y, x+200, y+140], 12, fill=(45,45,50), outline=(70,70,75), width=2)
    draw.rounded_rectangle([x+12, y+10, x+188, y+110], 6, fill=(220,230,255))
    # 键盘区域
    for row in range(3):
        for col in range(8):
            kx = x+50+col*18
            ky = y+118+row*14
            draw.rounded_rectangle([kx,ky, kx+12, ky+8], 2, fill=(60,60,65))

def draw_headphones(draw, x, y):
    """头戴式耳机"""
    # 头梁
    draw.arc([x+20, y, x+140, y+120], 0, 180, fill=(40,40,45), width=10)
    # 左耳罩
    draw.rounded_rectangle([x+5, y+60, x+45, y+120], 12, fill=(50,50,55))
    draw.ellipse([x+12, y+68, x+38, y+112], fill=(240,200,180))
    # 右耳罩
    draw.rounded_rectangle([x+115, y+60, x+155, y+120], 12, fill=(50,50,55))
    draw.ellipse([x+122, y+68, x+148, y+112], fill=(240,200,180))

def draw_mouse(draw, x, y):
    """画鼠标"""
    draw.ellipse([x+10, y, x+80, y+100], fill=(50,50,55))
    draw.ellipse([x+25, y+10, x+65, y+60], fill=(70,70,75))
    # 滚轮
    draw.rounded_rectangle([x+40, y+22, x+50, y+48], 4, fill=(200,200,200))
    # 线
    draw.line([x+45, y-20, x+45, y], fill=(50,50,55), width=4)

def draw_fridge(draw, x, y):
    """迷你冰箱"""
    w, h = 100, 160
    draw.rounded_rectangle([x,y, x+w, y+h], 10, fill=(220,230,240), outline=(180,190,200), width=2)
    # 门缝
    draw.line([x, y+80, x+w, y+80], fill=(200,210,220), width=2)
    # 把手
    draw.rounded_rectangle([x+w-8, y+20, x+w+5, y+60], 4, fill=(160,170,180))
    # 饮料
    draw.rounded_rectangle([x+30, y+50, x+48, y+72], 4, fill=(100,180,100))
    draw.rounded_rectangle([x+52, y+45, x+70, y+72], 4, fill=(255,150,80))

def draw_lamp(draw, x, y):
    """台灯"""
    # 底座
    draw.rounded_rectangle([x+60, y+140, x+110, y+150], 6, fill=(60,60,65))
    # 灯杆
    draw.line([x+85, y+140, x+85, y+60], fill=(80,80,85), width=6)
    draw.line([x+85, y+60, x+45, y+30], fill=(80,80,85), width=6)
    # 灯罩
    draw.polygon([x+20,y+30, x+70,y+10, x+50,y+35], fill=(255,245,200))
    draw.ellipse([x+30, y+20, x+60, y+45], fill=(255,252,240))

def draw_table(draw, x, y):
    """折叠桌"""
    # 桌面
    draw.rounded_rectangle([x+10,y+20, x+160,y+50], 8, fill=(200,160,120), outline=(160,120,80), width=2)
    # 桌腿 X
    draw.line([x+30,y+50, x+20,y+110], fill=(140,100,60), width=5)
    draw.line([x+140,y+50, x+150,y+110], fill=(140,100,60), width=5)
    draw.line([x+30,y+50, x+150,y+110], fill=(140,100,60), width=4)
    draw.line([x+140,y+50, x+20,y+110], fill=(140,100,60), width=4)

def draw_bedding(draw, x, y):
    """被子"""
    draw.rounded_rectangle([x+10,y+40, x+150,y+100], 14, fill=(240,220,240))
    # 折叠纹理
    for i in range(4):
        lx = x+30+i*35
        draw.line([lx, y+40, lx, y+100], fill=(220,200,220), width=2)
    # 枕头
    draw.rounded_rectangle([x+20,y+50, x+60,y+70], 10, fill=(255,255,255), outline=(230,230,230), width=1)

def draw_bike(draw, x, y):
    """自行车"""
    # 车身框架
    draw.line([x+40, y+30, x+120, y+30], fill=(220,80,60), width=5)
    draw.line([x+120,y+30, x+90, y+100], fill=(220,80,60), width=5)
    draw.line([x+40,y+30, x+50, y+100], fill=(220,80,60), width=5)
    # 轮子
    draw.ellipse([x+10, y+60, x+70, y+120], outline=(50,50,50), width=5)
    draw.ellipse([x+80, y+60, x+140, y+120], outline=(50,50,50), width=5)
    # 车座
    draw.ellipse([x+30,y+18, x+55, y+35], fill=(50,50,50))
    # 车把
    draw.line([x+115,y+30, x+130, y+15], fill=(50,50,50), width=4)
    draw.line([x+120,y+15, x+140, y+15], fill=(50,50,50), width=4)

def draw_racket(draw, x, y):
    """羽毛球拍 x2"""
    for ox in [0, 80]:
        # 拍框
        draw.ellipse([x+ox, y, x+ox+50, y+50], outline=(40,40,45), width=5)
        # 拍线横竖
        for lx in range(x+ox+10, x+ox+42, 8):
            draw.line([lx, y+8, lx, y+42], fill=(180,180,180), width=1)
        for ly in range(y+8, y+52, 7):
            draw.line([x+ox+10, ly, x+ox+40, ly], fill=(180,180,180), width=1)
        # 拍杆
        draw.line([x+ox+25, y+50, x+ox+25, y+85], fill=(80,80,85), width=4)
        # 握把
        draw.rounded_rectangle([x+ox+20, y+82, x+ox+30, y+100], 3, fill=(160,120,80))

def draw_yoga_mat(draw, x, y):
    """瑜伽垫卷"""
    draw.ellipse([x+50,y+10, x+110, y+70], fill=(100,180,150), outline=(60,140,110), width=3)
    draw.ellipse([x+55,y+15, x+105, y+65], fill=(120,200,170))
    # 卷轴
    draw.rounded_rectangle([x+50,y+65, x+110, y+130], 8, fill=(100,180,150))
    for i in range(4):
        ly = y+75+i*12
        draw.line([x+55, ly, x+105, ly], fill=(80,160,130), width=1)

def draw_basketball(draw, x, y):
    """篮球"""
    r = 60
    cx, cy = x+80, y+75
    draw.ellipse([cx-r, cy-r, cx+r, cy+r], fill=(200,130,40), outline=(160,90,20), width=3)
    # 球纹
    draw.arc([cx-30, cy-r+10, cx+30, cy+r-10], 0, 180, fill=(160,90,20), width=2)
    draw.arc([cx-r+10, cy-20, cx+r-10, cy+20], -90, 90, fill=(160,90,20), width=2)

def draw_shoe(draw, x, y):
    """Air Force 1"""
    pts = [(x+10,y+40), (x+10,y+80), (x+40,y+95), (x+120,y+80), (x+140,y+40),
           (x+130,y+30), (x+110,y+25), (x+60,y+30), (x+30,y+25)]
    draw.polygon(pts, fill=(245,245,245), outline=(200,200,200))
    # 鞋带
    for i in range(4):
        lx = x+40+i*20
        draw.line([lx, y+40, lx-10, y+55], fill=(220,220,220), width=2)
    # Swoosh
    draw.arc([x+50, y+55, x+110, y+85], -20, 80, fill=(40,40,40), width=4)

def draw_backpack(draw, x, y):
    """双肩包"""
    # 主体
    draw.rounded_rectangle([x+20,y+20, x+130,y+110], 14, fill=(160,180,140), outline=(120,140,100), width=2)
    # 前袋
    draw.rounded_rectangle([x+35,y+60, x+115,y+100], 8, fill=(150,170,130), outline=(120,140,100), width=2)
    # 扣子
    draw.ellipse([x+65,y+50, x+85, y+70], outline=(80,100,60), width=2)
    # 背带
    draw.arc([x+20,y, x+50, y+60], -90, 0, fill=(100,120,80), width=5)
    draw.arc([x+100,y, x+130, y+60], -180, -90, fill=(100,120,80), width=5)

def draw_earbuds(draw, x, y):
    """AirPods"""
    # 充电盒
    draw.rounded_rectangle([x+40,y+30, x+120,y+100], 14, fill=(245,245,245), outline=(210,210,210), width=2)
    # 盖子缝
    draw.line([x+40,y+52, x+120,y+52], fill=(200,200,200), width=2)
    # 指示灯
    draw.ellipse([x+75,y+36, x+85,y+46], fill=(100,255,100))
    # 耳机
    for ox in [x+48, x+92]:
        draw.ellipse([ox, y+62, ox+20, y+90], fill=(245,245,245), outline=(210,210,210), width=2)
        draw.rectangle([ox+2, y+88, ox+18, y+100], fill=(245,245,245))

def draw_charger(draw, x, y):
    """充电器"""
    draw.rounded_rectangle([x+30,y+20, x+100,y+90], 12, fill=(50,50,55))
    # USB口
    for i in range(3):
        ux = x+40+i*20
        draw.rounded_rectangle([ux, y+60, ux+14, y+78], 3, fill=(30,30,35))
    # 插头
    draw.rectangle([x+55, y+90, x+75, y+105], fill=(60,60,65))
    draw.rectangle([x+60, y+103, x+70, y+115], fill=(180,180,180))
    draw.rectangle([x+55, y+103, x+75, y+115], fill=(180,180,180))

def draw_lipstick(draw, x, y):
    """口红"""
    # 管身
    draw.rounded_rectangle([x+60,y+30, x+120,y+110], 8, fill=(40,35,45))
    # 金色环
    draw.rounded_rectangle([x+58,y+28, x+122,y+42], 5, fill=(200,170,80))
    draw.rounded_rectangle([x+58,y+100, x+122,y+114], 5, fill=(200,170,80))
    # 膏体
    draw.rounded_rectangle([x+68,y+8, x+112,y+42], 6, fill=(210,80,80))
    # 高光
    draw.rounded_rectangle([x+75,y+12, x+90,y+36], 4, fill=(240,120,120,100))

def draw_projector(draw, x, y):
    """投影仪"""
    draw.rounded_rectangle([x+15,y+30, x+145,y+90], 14, fill=(40,40,45))
    # 镜头
    draw.ellipse([x+25,y+40, x+55,y+70], fill=(30,30,35), outline=(80,80,85), width=2)
    draw.ellipse([x+32,y+47, x+48,y+63], fill=(20,100,200,150))
    # 散热孔
    for i in range(6):
        draw.line([x+65, y+42+i*6, x+100, y+42+i*6], fill=(60,60,65), width=2)
    # 光束
    pts = [(x+55,y+55), (x+180,y+20), (x+180,y+90)]
    draw.polygon(pts, fill=(255,255,225))

def draw_guitar(draw, x, y):
    """吉他"""
    # 琴身
    draw.ellipse([x+60,y+50, x+80,y+70], fill=(220,180,140))
    draw.ellipse([x+30,y+40, x+130,y+100], fill=(220,180,140), outline=(180,140,100), width=2)
    # 音孔
    draw.ellipse([x+60,y+55, x+100,y+85], fill=(50,30,20))
    # 琴颈
    draw.rectangle([x+135,y+58, x+175,y+72], fill=(180,140,100))
    # 琴弦
    for i in range(3):
        draw.line([x+135,y+60+i*3, x+175,y+60+i*3], fill=(200,200,200), width=1)

def draw_notebook(draw, x, y):
    """笔记本/复习书"""
    draw_rounded_rect = lambda xy,r,**kw: draw.rounded_rectangle(xy,r,**kw)
    # 书堆
    colors = [(80,80,180), (210,80,60), (60,160,120)]
    for i,(cx,cy,cw,ch,cc) in enumerate([
            (x+10,y+20, x+140,y+28, colors[0]),
            (x+25,y+35, x+130,y+42, colors[1]),
            (x+5,y+48, x+150,y+55, colors[2])]):
        draw.rectangle([cx,cy,cw,ch], fill=cc, outline=darken(cc,0.3))
    # 笔记线条
    for i in range(4):
        lx = x+45+i*22
        draw.line([lx, y+25, lx, y+75], fill=(255,255,255), width=2)

def darken(color, factor=0.7):
    return tuple(max(0, min(255, int(c*factor))) for c in color[:3])


# ============================================================
# 商品 -> 绘图函数映射
# ============================================================
GOODS = [
    (1, "高等数学第七版", "教材教辅", 25), (2, "数据结构", "教材教辅", 20),
    (3, "大学英语四级真题", "教材教辅", 15), (4, "考研政治复习全书", "教材教辅", 35),
    (5, "iPad Air 5", "电子产品", 2800), (6, "华为MateBook 14", "电子产品", 4200),
    (7, "索尼WH-1000XM5", "电子产品", 1200), (8, "罗技MX Master 3S", "电子产品", 350),
    (9, "宿舍用小冰箱", "生活用品", 150), (10, "北欧风台灯", "生活用品", 45),
    (11, "折叠床上桌", "生活用品", 35), (12, "加厚保暖棉被", "生活用品", 80),
    (13, "永久山地自行车", "运动户外", 380), (14, "李宁羽毛球拍", "运动户外", 180),
    (15, "Keep瑜伽垫", "运动户外", 40), (16, "篮球斯伯丁", "运动户外", 60),
    (17, "Nike Air Force 1", "服饰鞋包", 380), (18, "北极狐双肩包", "服饰鞋包", 280),
    (19, "AirPods Pro 2", "数码配件", 999), (20, "Anker充电器", "数码配件", 55),
    (21, "MAC子弹头口红", "美妆个护", 85), (22, "小明Q2投影仪", "其他", 350),
    (23, "Yamaha F310吉他", "其他", 380), (24, "男大原味篮球鞋", "运动户外", 99),
]

DRAW_FN = {
    "教材教辅": draw_book,
    "电子产品": draw_phone,
    "生活用品": draw_fridge,
    "运动户外": draw_bike,
    "服饰鞋包": draw_shoe,
    "数码配件": draw_earbuds,
    "美妆个护": draw_lipstick,
    "其他": draw_guitar,
}

# 特定商品覆盖
SPECIAL = {
    3: draw_notebook, 4: draw_notebook,
    5: draw_phone, 6: draw_laptop, 7: draw_headphones, 8: draw_mouse,
    9: draw_fridge, 10: draw_lamp, 11: draw_table, 12: draw_bedding,
    13: draw_bike, 14: draw_racket, 15: draw_yoga_mat, 16: draw_basketball,
    17: draw_shoe, 18: draw_backpack, 19: draw_earbuds, 20: draw_charger,
    21: draw_lipstick, 22: draw_projector, 23: draw_guitar, 24: draw_basketball,
}

# ============================================================
# 生成单张图片
# ============================================================
def make_image(goods_id, title, category, price, variant=0):
    W, H = 800, 800
    img = Image.new('RGB', (W, H), (248, 249, 252))
    draw = ImageDraw.Draw(img)

    # 柔和背景格子
    for gy in range(0, H, 40):
        draw.line([0, gy, W, gy], fill=(235,237,245), width=1)

    # 绘制商品图标
    fn = SPECIAL.get(goods_id, DRAW_FN.get(category, draw_book))
    fn(draw, W//2-80, H//2-120)

    # 标题
    try: ft = ImageFont.truetype('C:/Windows/Fonts/msyh.ttc', 24)
    except: ft = FONTS['title']
    bbox = draw.textbbox((0,0), title, font=ft)
    tw = bbox[2]-bbox[0]
    draw.text((W//2 - tw//2, H-180), title, fill=BLACK, font=ft)

    # 分类
    try: fs = ImageFont.truetype('C:/Windows/Fonts/msyh.ttc', 16)
    except: fs = ft
    label = f"[{category}]  校园二手交易平台"
    bbox = draw.textbbox((0,0), label, font=fs)
    tw = bbox[2]-bbox[0]
    draw.text((W//2 - tw//2, H-145), label, fill=GRAY, font=fs)

    # 价格标签
    price_text = f"¥{price}"
    try: fp = ImageFont.truetype('C:/Windows/Fonts/msyhbd.ttf', 36)
    except: fp = FONTS['price']
    bbox = draw.textbbox((0,0), price_text, font=fp)
    tw = bbox[2]-bbox[0]
    # 价格背景
    draw.rounded_rectangle(
        [W//2 - tw//2 - 20, H-100, W//2 + tw//2 + 20, H-45],
        16, fill=(239,68,68))
    draw.text((W//2 - tw//2, H-95), price_text, fill=WHITE, font=fp)

    # 变体变换
    if variant == 1:
        e = ImageEnhance.Brightness(img)
        img = e.enhance(1.1)
    elif variant == 2:
        e = ImageEnhance.Color(img)
        img = e.enhance(1.2)

    return img


# ============================================================
print("🎨 生成商品插图...\n")
import subprocess

for goods_id, title, category, price in GOODS:
    print(f"  📸 #{goods_id} {title}...", end=" ")
    for v in range(3):
        fname = f"goods_{goods_id}_{v+1}.jpg"
        img = make_image(goods_id, title, category, price, v)
        img.save(os.path.join(UPLOAD_DIR, fname), 'JPEG', quality=92)
    print("✅")

print(f"\n✅ 全部完成！24×3=72 张插图已生成")
print("🌐 刷新 http://localhost:5174 查看")
