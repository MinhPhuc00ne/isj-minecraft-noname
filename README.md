# 🔮 Dark Gathering - Minecraft Mod (1.20.1 Forge)

[![Minecraft Version](https://img.shields.io/badge/Minecraft-1.20.1-brightgreen.svg)](https://minecraft.net)
[![Forge Version](https://img.shields.io/badge/Forge-47.2.0+-orange.svg)](https://files.minecraftforge.net/)
[![Java](https://img.shields.io/badge/Java-17-blue.svg)](https://www.oracle.com/java/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

Mod Minecraft 1.20.1 lấy cảm hứng từ bộ manga/anime **Dark Gathering** (ダークギャザリング). Mod mang đến cơ chế nhập vai Âm Dương Sư, hệ thống Mana, Sách Ác Linh, 9 ô Kỹ năng tùy chỉnh và loại bỏ hoàn toàn các Mob phản diện mặc định của Minecraft.

---

## 🌟 Tính Năng Chính (Key Features)

### 1. 📖 Sách Đầu Game (`Dark Gathering Book`)
- Ngay khi vào thế giới (Survival hoặc Creative), người chơi sẽ nhận được cuốn **Sách Dark Gathering**.
- Mở sách lần đầu để chọn Lớp Nhân Vật:
  - **Âm Dương Sư (Onmyoji)**: Được trao thị lực nhìn đêm vĩnh viễn, khả năng nhìn thấy và tấn công tất cả các Ác Linh. Cuốn sách sẽ được đổi tên thành `Book of <Tên_Của_Bạn>`.
  - **Ác Linh (Evil Spirit)**: Tham gia vào con đường của các thực thể bóng tối.
  - **Bỏ qua (Vanilla Mode)**: Giữ nguyên chế độ chơi bình thường.

### 2. 👻 Cơ Chế Ác Linh & Thay Đổi Mob Vanilla
- **Loại bỏ Mob Vanilla**: Toàn bộ mob phản diện tự nhiên (Zombie, Skeleton, Creeper, Enderman, Spider...) sẽ **không sinh tự nhiên** trong thế giới. Chúng chỉ xuất hiện khi được người chơi đặt ra bằng Trứng ở chế độ Sáng Tạo.
- **Tầm nhìn Ác Linh**: Người chơi không phải Âm Dương Sư sẽ **không thể nhìn thấy và không thể tấn công** Ác Linh cho đến khi Ác Linh đó bị rút xuống **tầm <= 3 HP** (hoặc được spawn ra từ trứng ở chế độ Sáng Tạo).

### 3. ⚔️ Thanh Kỹ Năng 9 Ô & Phím ALT
- Mở Sách để kéo thả/gán các kỹ năng đã mở khóa vào 9 ô skill.
- Nhấn phím **ALT** (trái) để chuyển đổi giữa **Thanh Đồ Dùng (Item Hotbar)** và **Thanh Kỹ Năng (Skill Hotbar)**.
- Khi bật Thanh Kỹ Năng, bấm các phím số **1-9** để kích hoạt ngay kỹ năng tương ứng.

### 4. 💙 Hệ Thống Mana & Lệnh Command
- Thanh Mana hiển thị góc dưới màn hình:
  - Mặc định **100 MP**.
  - Max Mana tăng dần theo Level Kinh Nghiệm (XP): `Max Mana = 100 + (Level XP * 10)`.
  - Tự động hồi Mana theo thời gian.
- **Lệnh Command**:
  - `/maxmana <người_chơi> <số_mana>`: Đặt số lượng Mana tối đa cho người chơi (Ví dụ: `/maxmana @s 9999999`).

---

## 🛠️ Hướng Dẫn Cài Đặt (Installation)

### Dành cho Người Chơi (SKLauncher / TLauncher / Launcher Chuẩn)
1. Tải và cài đặt **Minecraft Forge 1.20.1** (Phiên bản Forge 47.2.0 trở lên).
2. Tải file mod `.jar` từ mục Releases.
3. Bỏ file `.jar` vào thư mục `.minecraft/mods`.
4. Mở SKLauncher, chọn Profile Forge 1.20.1 và nhấn Play!

### Dành cho Lập Trình Viên (Development Environment)
1. Clone dự án:
   ```bash
   git clone https://github.com/MinhPhuc00ne/isj-minecraft-noname.git
   cd isj-minecraft-noname
   ```
2. Import dự án vào **IntelliJ IDEA** hoặc **Eclipse** dưới dạng dự án **Gradle**.
3. Chạy lệnh setup run configs:
   ```bash
   ./gradlew genIntellijRuns    # Dành cho IntelliJ IDEA
   ./gradlew genEclipseRuns     # Dành cho Eclipse
   ```
4. Chạy `runClient` để test trực tiếp mod trong IDE.

---

## 📂 Cấu Trúc Mã Nguồn (Project Architecture)

```
src/main/java/com/yourname/darkgathering/
├── DarkGatheringMod.java           # Entry point chính của Mod
├── capability/                     # Dữ liệu Player Persistence (Class, Mana, Skills)
│   ├── IPlayerData.java
│   ├── PlayerData.java
│   └── PlayerDataProvider.java
├── client/                         # Giao diện & Điều khiển (Client Side)
│   ├── KeyBindings.java            # Đăng ký nút ALT & Phím số 1-9
│   ├── gui/
│   │   ├── ClassSelectionScreen.java
│   │   ├── BookSkillScreen.java
│   │   └── ManaAndSkillOverlay.java
│   └── renderer/
│       └── EvilSpiritRenderer.java
├── command/                        # Lệnh Command
│   └── MaxManaCommand.java
├── entity/                         # Custom Mob Entity (Ác Linh)
│   ├── ModEntities.java
│   └── EvilSpiritEntity.java
├── event/                          # Xử lý các sự kiện trong Game
│   ├── ModEvents.java
│   ├── MobSpawnEvents.java
│   └── SpiritVisibilityEvents.java
├── item/                           # Vật phẩm
│   ├── ModItems.java
│   └── DarkGatheringBookItem.java
├── network/                        # Gói tin đồng bộ Server <-> Client
│   ├── PacketHandler.java
│   ├── C2SSelectClassPacket.java
│   ├── C2SSyncSkillSlotsPacket.java
│   ├── C2SCastSkillPacket.java
│   └── S2CSyncPlayerDataPacket.java
└── skill/                          # Hệ thống Kỹ năng Ác Linh
    ├── Skill.java
    └── SkillRegistry.java
```

---

## 📜 Giấy Phép (License)

Dự án này được phân phối dưới giấy phép **MIT License**. Chi tiết xem tại file [LICENSE](LICENSE).
