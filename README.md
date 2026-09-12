# 🥊 Infinity Gauntlet Mod - Minecraft 1.20.1 Forge

[![Minecraft Version](https://img.shields.io/badge/Minecraft-1.20.1-brightgreen.svg)](https://minecraft.net)
[![Forge Version](https://img.shields.io/badge/Forge-47.4.23+-orange.svg)](https://files.minecraftforge.net/)
[![Java](https://img.shields.io/badge/Java-17-blue.svg)](https://www.oracle.com/java/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

Mod **Găng Tay Vô Cực (Infinity Gauntlet)** dành cho Minecraft 1.20.1 Forge với khả năng búng tay tiêu diệt sinh vật 100 blocks, chọn chế độ qua phím `PgUp`, hiệu ứng bất tử và tối ưu hóa đa luồng Async cho CPU đa nhân.

---

## 🌟 Tính Năng Chính (Key Features)

### 1. 🥊 Găng Tay Vô Cực (Infinity Gauntlet Item)
- Thiết kế hình ảnh chi tiết mạ vàng đính 6 viên đá vô cực (Power, Space, Reality, Soul, Time, Mind).
- Hiển thị chuẩn nét trong túi đồ (Inventory), khi cầm trên tay (First-person & Third-person) và khi thả ra đất (Dropped item).

### 2. ⚡ Hiệu Ứng Bất Tử & Tự Động Hồi Máu (God Mode)
- **Tự động hồi 100% máu tức thì**: Bất kể khi nào máu bị giảm dưới 100%, hệ thống sẽ lập tức hồi lại 100% máu.
- **Bất tử 100%**: Kháng tất cả sát thương từ quái vật/sinh vật, chống ngã từ mọi độ cao, không bị cháy/thương do Lửa & Lava, và thở tự do dưới nước.
- **Full hiệu ứng tốt nhất**: Liên tục cấp hiệu ứng Kháng sát thương V, Hồi phục V, Kháng lửa, Thở dưới nước, Nhìn đêm, Sức mạnh X, Đào nhanh V, Tốc độ II, No nê V, Máu vàng V.

### 3. 🖥️ Phím Tắt `PgUp` & Giao Diện Chọn Đá (Interactive Screen)
- Nhấn nút **`PgUp`** (`PAGE_UP`) để mở bảng giao diện chọn chức năng viên đá vô cực hoặc **Chế độ 6 Viên Đá (Búng tay / Snap)**.
- Khi chưa chọn chế độ, nhấp chuột phải sẽ hiện thông báo yêu cầu người chơi mở menu chọn trước.

### 4. 🫰 Búng Tay Tiêu Diệt 100 Blocks (Snap Ability)
- Khi chọn chế độ 6 Viên Đá (Snap) và click chuột phải:
  - Phát âm thanh sấm sét & wither, cùng hiệu ứng chùm hạt rực rỡ.
  - Tiêu diệt tất cả sinh vật trong phạm vi **100 blocks** tính từ người chơi.
  - Xóa sạch mọi vật phẩm rơi rớt dưới đất trong 100 blocks để tránh gây tràn 8GB RAM hoặc nghẽn card đồ họa iGPU.

### 5. 🚀 Tối Ưu Hóa Đa Luồng Async
- Sử dụng Java `CompletableFuture` tính toán quét thực thể 100 blocks ngoài luồng Server Tick (async multithreading), tận dụng tối đa sức mạnh của CPU đa nhân (Ryzen 7 5700U 16 threads).

---

## 📂 Cấu Trúc Mã Nguồn (Project Architecture)

```
src/main/java/com/minhphuc/infinitygauntlet/
├── InfinityGauntletMod.java               # Entry point chính của Mod
├── client/
│   ├── ModKeyBindings.java                # Đăng ký phím tắt PgUp
│   ├── ClientInputEvents.java             # Xử lý sự kiện bấm phím PgUp
│   └── gui/
│       └── InfinityStoneSelectScreen.java # Giao diện chọn viên đá Vô Cực
├── event/
│   └── PlayerEvents.java                  # Xử lý sự kiện Bất tử & Chống sát thương
├── item/
│   ├── ModItems.java                      # Đăng ký Items & Creative Tab
│   └── InfinityGauntletItem.java          # Item Găng Tay Vô Cực & Búng tay Snap
└── network/
    ├── ModMessages.java                   # Đăng ký Kênh mạng Forge
    └── ServerboundSelectModePacket.java   # Gói tin gửi chế độ từ Client lên Server
```

---

## 📜 Giấy Phép (License)

Dự án này được phân phối dưới giấy phép **MIT License**.
