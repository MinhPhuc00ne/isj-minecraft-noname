# ⚔️ Weapons Mod - Minecraft 1.20.1 Forge

[![Minecraft Version](https://img.shields.io/badge/Minecraft-1.20.1-brightgreen.svg)](https://minecraft.net)
[![Forge Version](https://img.shields.io/badge/Forge-47.4.23+-orange.svg)](https://files.minecraftforge.net/)
[![Java](https://img.shields.io/badge/Java-17-blue.svg)](https://www.oracle.com/java/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

Mod **Vũ Khí (Weapons Mod)** dành cho Minecraft 1.20.1 Forge - Hệ thống tổng hợp các loại vũ khí đặc biệt (như Găng Tay Vô Cực, và nhiều vũ khí siêu cấp khác trong tương lai).

---

## 🌟 Các Vũ Khí & Tính Năng (Weapons & Features)

### 🥊 Module: Găng Tay Vô Cực (Infinity Gauntlet)
- **Thiết kế hình ảnh chi tiết**: Mạ vàng đính 6 viên đá vô cực (Power, Space, Reality, Soul, Time, Mind).
- **God Mode & Bất Tử**: Tự động hồi 100% máu tức thì, miễn nhiễm 100% mọi loại sát thương, té ngã, Lửa/Lava và ngạt nước.
- **Phím Tắt `PgUp` & Screen Chọn Đá**: Mở bảng GUI chọn các chế độ viên đá hoặc chế độ **Sức mạnh 6 Viên Đá (Snap)**.
- **Búng Tay Tiêu Diệt 100 Blocks**: Quét sạch toàn bộ sinh vật và vật phẩm rơi vãi trong phạm vi 100 blocks.
- **Tối ưu Async**: Tính toán đa luồng `CompletableFuture` tận dụng CPU đa nhân.

---

## 📂 Cấu Trúc Mã Nguồn Modular (Project Architecture)

```
src/main/java/com/minhphuc/weapons/
├── WeaponsMod.java                            # Entry point chính của Mod
├── init/
│   ├── ModItems.java                          # Đăng ký Items & Creative Tab
│   ├── ModKeyBindings.java                    # Đăng ký phím tắt PgUp
│   └── ModMessages.java                       # Đăng ký Kênh mạng Forge
├── client/
│   └── ClientInputEvents.java                 # Xử lý sự kiện bấm phím Client
└── content/
    └── infinitygauntlet/                      # Module Găng Tay Vô Cực
        ├── InfinityGauntletItem.java          # Item Găng Tay Vô Cực & Búng tay Snap
        ├── InfinityStoneSelectScreen.java     # Giao diện GUI chọn chế độ đá
        ├── InfinityGauntletEvents.java        # Sự kiện Bất tử & Miễn nhiễm sát thương
        └── ServerboundSelectModePacket.java   # Gói tin mạng chuyển chế độ
```

---

## 📜 Giấy Phép (License)

Dự án này được phân phối dưới giấy phép **MIT License**.
