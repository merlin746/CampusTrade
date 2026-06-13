#!/usr/bin/env python3
"""
为22件商品批量生成精美占位图
"""
import os, random, textwrap
from PIL import Image, ImageDraw, ImageFont, ImageFilter, ImageEnhance

UPLOAD_DIR = './uploads'
os.makedirs(UPLOAD_DIR, exist_ok=True)

GOODS = [
    (1, "高等数学第七版（同济大学）", "教材教辅"),
    (2, "数据结构（C语言版）严蔚敏", "教材教辅"),
    (3, "大学英语四级真题（2025版）", "教材教辅"),
    (4, "考研政治复习全书（肖秀荣）", "教材教辅"),
    (5, "iPad Air 5 64G WiFi版", "电子产品"),
    (6, "华为MateBook 14 2024款", "电子产品"),
    (7, "索尼WH-1000XM5降噪耳机", "电子产品"),
    (8, "罗技MX Master 3S无线鼠标", "电子产品"),
    (9, "宿舍用小冰箱 30L", "生活用品"),
    (10, "北欧风台灯 LED护眼灯", "生活用品"),
    (11, "折叠床上桌 笔记本电脑桌", "生活用品"),
    (12, "加厚保暖棉被 冬季宿舍用", "生活用品"),
    (13, "永久牌山地自行车 26寸21速", "运动户外"),
    (14, "李宁羽毛球拍 雷霆80一对", "运动户外"),
    (15, "Keep瑜伽垫 加厚防滑", "运动户外"),
    (16, "篮球 斯伯丁 74-604Y", "运动户外"),
    (17, "Nike Air Force 1 纯白 40码", "服饰鞋包"),
    (18, "北极狐 Kanken 双肩包 16L", "服饰鞋包"),
    (19, "AirPods Pro 2代 USB-C版", "数码配件"),
    (20, "Anker 65W 氮化镓充电器 三口", "数码配件"),
    (21, "MAC 子弹头口红 #316", "美妆个护"),
    (22, "宿舍用小型投影仪 小明Q2", "其他"),
    (23, "吉他 Yamaha F310 民谣吉他", "其他"),
]

COLOR_SCHEMES = {
    "教材教辅": [("#6366f1", "#8b5cf6"), ("#e8637a", "#f5576c"), ("#4facfe", "#6366f1")],
    "电子产品": [("#2d3436", "#636e72"), ("#1e293b", "#334155"), ("#0f172a", "#1e293b")],
    "生活用品": [("#f093fb", "#f5576c"), ("#a18cd1", "#fbc2eb"), ("#fad0c4", "#ffd1ff")],
    "运动户外": [("#11998e", "#38ef7d"), ("#f12711", "#f5af19"), ("#00b4db", "#0083b0")],
    "服饰鞋包": [("#ee9ca7", "#ffdde1"), ("#a770ef", "#cf8bf3"), ("#d3cce3", "#e9e4f0")],
    "数码配件": [("#434343", "#000000"), ("#232526", "#414345"), ("#141e30", "#243b55")],
    "美妆个护": [("#f953c6", "#b91d73"), ("#ff758c", "#ff7eb3"), ("#e55d87", "#5fc3e4")],
    "其他":     [("#667eea", "#764ba2"), ("#fcb045", "#fd1d1d"), ("#0093e9", "#80d0c7")],
}

EMOJI_MAP = {
    "教材教辅": "📚", "电子产品": "💻", "生活用品": "🏠",
    "运动户外": "⚽", "服饰鞋包": "👗", "数码配件": "🎧",
    "美妆个护": "💄", "其他": "📦",
}

# 中文字体
try:
    FONT_TITLE = ImageFont.truetype("C:/Windows/Fonts/msyhbd.ttf", 38)
    FONT_TEXT = ImageFont.truetype("C:/Windows/Fonts/msyh.ttc", 24)
    FONT_SMALL = ImageFont.truetype("C:/Windows/Fonts/msyh.ttc", 18)
    FONT_EMOJI = ImageFont.truetype("C:/Windows/Fonts/seguiemj.ttf", 72)
except:
    FONT_TITLE = FONT_TEXT = FONT_SMALL = FONT_EMOJI = ImageFont.load_default()


def hex_to_rgb(hex_color):
    return tuple(int(hex_color[i:i+2], 16) for i in (1,3,5))


def make_image(title, category, price, shot=0):
    """生成一张800×800的商品展示图"""
    W, H = 800, 800
    colors = random.choice(COLOR_SCHEMES.get(category, [("#667eea", "#764ba2")]))
    c1, c2 = hex_to_rgb(colors[0]), hex_to_rgb(colors[1])

    img = Image.new('RGB', (W, H))
    draw = ImageDraw.Draw(img)

    # ---- 渐变背景 ----
    for y in range(H):
        ratio = y / H
        r = int(c1[0] * (1-ratio) + c2[0] * ratio)
        g = int(c1[1] * (1-ratio) + c2[1] * ratio)
        b = int(c1[2] * (1-ratio) + c2[2] * ratio)
        for x in range(W):
            img.putpixel((x, y), (r, g, b))

    # ---- 圆形光晕 ----
    for _ in range(3):
        cx = random.randint(100, W-100)
        cy = random.randint(100, H-100)
        r = random.randint(150, 350)
        for y in range(max(0, cy-r), min(H, cy+r)):
            for x in range(max(0, cx-r), min(W, cx+r)):
                if (x-cx)**2 + (y-cy)**2 < r**2:
                    p = img.getpixel((x, y))
                    brightness = 20 if random.random() < 0.3 else 5
                    img.putpixel((x, y), tuple(
                        max(0, min(255, c + brightness)) for c in p
                    ))

    # 轻微模糊
    img = img.filter(ImageFilter.GaussianBlur(radius=2))

    draw = ImageDraw.Draw(img)

    # ---- 毛玻璃卡片 ----
    card_x, card_y = 50, 240
    card_w, card_h = W - 100, 280
    for _ in range(2):
        draw.rounded_rectangle(
            [card_x, card_y, card_x+card_w, card_y+card_h],
            radius=30,
            fill=(255,255,255,25) if _ == 0 else None,
            outline=(255,255,255,40),
            width=1
        )

    # ---- 大 Emoji ----
    emoji = EMOJI_MAP.get(category, "📦")
    try:
        bbox = draw.textbbox((0,0), emoji, font=FONT_EMOJI)
        tw = bbox[2] - bbox[0]
        draw.text((W//2 - tw//2, 70), emoji, fill=(255,255,255), font=FONT_EMOJI)
    except:
        pass

    # ---- 标题（换行） ----
    lines = textwrap.wrap(title, width=11)
    title_y = card_y + 30
    for line in lines[:3]:
        bbox = draw.textbbox((0,0), line, font=FONT_TITLE)
        tw = bbox[2] - bbox[0]
        draw.text((W//2 - tw//2, title_y), line,
                  fill=(255,255,255), font=FONT_TITLE,
                  stroke_width=2, stroke_fill=(0,0,0,30))
        title_y += 50

    # ---- 分类标签 ----
    tag = f"🏷 {category}"
    bbox = draw.textbbox((0,0), tag, font=FONT_TEXT)
    tw = bbox[2] - bbox[0]
    draw.text((W//2 - tw//2 + 10, title_y + 10), tag,
              fill=(255,255,255,200), font=FONT_TEXT)

    # ---- 价格 ----
    price_text = f"¥{price}"
    bbox = draw.textbbox((0,0), price_text, font=FONT_TITLE)
    tw = bbox[2] - bbox[0]
    draw.text((W//2 - tw//2, title_y + 55), price_text,
              fill=(255,255,255), font=FONT_TITLE,
              stroke_width=3, stroke_fill=(0,0,0,40))

    # ---- 品牌水印 ----
    brand = "🏫 校园二手交易平台"
    bbox = draw.textbbox((0,0), brand, font=FONT_SMALL)
    tw = bbox[2] - bbox[0]
    draw.text((W//2 - tw//2, H - 60), brand,
              fill=(255,255,255,100), font=FONT_SMALL)

    # ---- 对角小装饰 ----
    for i in range(0, W, 60):
        draw.ellipse([i, 0, i+3, 3], fill=(255,255,255,40))

    return img


def make_variants(img):
    """从主图生成3张变体"""
    variants = []
    # 变体1: 主图本身就是
    variants.append(img)

    # 变体2: 提亮
    e = ImageEnhance.Brightness(img)
    v2 = e.enhance(1.2)
    variants.append(v2)

    # 变体3: 增加对比度+饱和度
    e2 = ImageEnhance.Color(img)
    v3 = e2.enhance(1.4)
    variants.append(v3)

    return variants


# ============================================================
print("🎨 开始生成商品图片...\n")
import subprocess

for goods_id, title, category in GOODS:
    # 读价格
    cmd = f'mysql -u root -p060423 --default-character-set=utf8mb4 -N -e "SELECT price FROM school_trade.goods WHERE id={goods_id}" 2>&1'
    result = subprocess.run(cmd, shell=True, capture_output=True, text=True)
    price_str = result.stdout.strip()
    try:
        price = int(float(price_str))
    except:
        price = 99

    print(f"  📸 #{goods_id} | {category} | ¥{price} | {title[:20]}...")

    # 生成主图
    main_img = make_image(title, category, price, shot=0)
    variants = make_variants(main_img)

    for vi, variant in enumerate(variants):
        filename = f"goods_{goods_id}_{vi+1}.jpg"
        filepath = os.path.join(UPLOAD_DIR, filename)
        variant.save(filepath, 'JPEG', quality=90)

        # 插入 goods_image 表
        img_url = f"/uploads/{filename}"
        escaped = img_url.replace("\\", "\\\\")
        cmd = f'mysql -u root -p060423 -e "INSERT INTO school_trade.goods_image (goods_id, image_url, sort_order) VALUES ({goods_id}, \'{escaped}\', {vi})" 2>&1'
        subprocess.run(cmd, shell=True, capture_output=True)

    print(f"     ✅ 生成3张图片")

print(f"\n{'='*60}")
print("✅ 全部完成！每件商品已配3张精美占位图")
print(f"📁 图片位置: {os.path.abspath(UPLOAD_DIR)}")
print("🌐 刷新 http://localhost:5174 查看效果")
