# 📖 Hướng Dẫn Chi Tiết Mod "The Backrooms" (Minecraft 1.20.1 Forge)

Tài liệu tóm tắt toàn bộ tính năng, kiến trúc thế giới, vật phẩm và cấu trúc mã nguồn của Mod **The Backrooms**. Hướng dẫn này phản ánh chính xác nhất cấu trúc mã nguồn và thuật toán sinh thế giới 3D v2 mới nhất.

---

## 🛠️ 1. Giới Thiệu & Cách Vào Thế Giới Backrooms

Mod tái hiện chiều không gian đô thị huyền bí **Level 0 (The Yellow Rooms)** với tiếng đèn huỳnh quang vo ve liên tục, không gian cô độc, bẫy nguy hiểm và các phòng mê cung rộng lớn nối tiếp nhau vô tận.

### 🚪 Cách Dựng Cổng Dịch Chuyển (Portal):
1. **Dựng Khung**: Dùng khối **Khung Cổng Backrooms** (`backrooms_frame`) hoặc **Tường Giấy Vàng** (`yellow_wallpaper`) dựng khung hình chữ nhật đứng kích thước tối thiểu **4x5** (khoảng trống bên trong 2x3).
2. **Kích Hoạt**: Dùng **Bật lửa (Flint and Steel)** hoặc **Dụng cụ châm lửa Backrooms** (`backrooms_igniter`) nhấp chuột phải vào khung -> Cổng màu vàng ảo diệu sẽ xuất hiện.
3. **Chuyển Cảnh**: Bước vào cổng để dịch chuyển sang **The Backrooms Level 0**. Bước vào cổng ở Backrooms để trở về **Overworld**.

---

## 🏛️ 2. Kiến Trúc Thế Giới 15x15 & Các Biome Không Gian Siêu Rộng

Thế giới Backrooms v2 được mở rộng quy mô lưới cell lên **15x15 block** với hệ thống **Chiều cao động (Dynamic Height 4..15 block)**:

1. **Phòng Vàng Tiêu Chuẩn (Level 0 Monoyellow Rooms)**: Căn phòng vuông 15x15 block, trần thạch cao (`ceiling_tile`), tường dán giấy vàng họa tiết mono (`yellow_wallpaper`), nẹp chân tường gỗ (`wallpaper_baseboard`) và sàn thảm ẩm ướt (`moist_carpet`).
2. **Hành Lang Hẹp Chật Hẹp (Narrow Corridors)**: Lối đi tù túng kéo dài với chiều cao 3-4 block mang cảm giác ngột ngạt cổ điển.
3. **Hành Lang Dài & Rộng Lớn (Long Grand Corridors)**: Lối đi cực rộng **15 block** kéo dài qua nhiều ô cell, trần nâng cao **8 block (Y=73)** cùng dải đèn huỳnh quang (`fluorescent_light` / `flickering_light`) đôi chiếu sáng chớp tắt.
4. **Đại Sảnh / Căn Phòng Rộng Lớn (Wide Great Halls)**: Căn phòng không gian mở liên hoàn diện tích 15x15 block trở lên, trần nâng cao **10 block (Y=75)**, có 4 cột thạch cao chịu lực đối ứng và lưới đèn huỳnh quang diện rộng.
5. **Siêu Biome Rừng Tối Trong Nhà (Massive Dark Indoor Forest Biome)**: Rừng hoang hóa khổng lồ rộng lớn liên hoàn (diện tích 30x30 đến 45x45 block+), trần cao vượt trội **15 block (Y=79, trần Y=80)**, tường phủ rêu (`mossy_wallpaper`), nền đất ẩm `dark_moist_earth`, mọc dày đặc cây sồi (`Oak`) cao 6-10 block và thông (`Spruce`) cao 8-12 block tán lá sum suê như Overworld, cùng cỏ dại, nấm đỏ và nấm nâu.
6. **Khu Làng Bỏ Hoang Đa Dạng (1 đến 5 Nhà Độc Lập & Loot Khác Nhau)**: Khu vực làng hoang hóa chứa từ **1 đến 5 ngôi nhà** với 5 kiến trúc và vật phẩm loot khác nhau:
   - 🏡 *Nhà 1 (Peaked Cottage)*: Mái dốc sồi 5x5, rương chứa Chai nước hạnh nhân (`almond_water`) & Băng Cassette #01 (`cassette_tape_1`).
   - 🛖 *Nhà 2 (Nhà chữ L có hiên)*: 7x5 có hàng rào gỗ sồi (`oak_fence`), rương chứa Dụng cụ châm lửa (`backrooms_igniter`) & Đèn pin (`flashlight`).
   - 🏰 *Nhà 3 (Tháp quan sát / Thư viện 2 tầng)*: Tháp cao 7 block với kệ sách (`bookshelf`), rương chứa Băng Cassette #02 & #03.
   - ⚒️ *Nhà 4 (Xưởng thợ rèn lò nung)*: Lò nung đá cuội (`furnace`) & bàn chế tạo (`crafting_table`), rương chứa Đèn pin & dụng cụ.
   - 🌾 *Nhà 5 (Kho bạt gỗ / Open Barn)*: Khung xà gỗ sồi & khối rơm (`hay_block`), rương chứa Biển cảnh báo (`backrooms_sign`), Máy cassette (`cassette_player`) & Chai nước hạnh nhân.

---

## 🚪 3. Bộ 4 Loại Cửa Văn Phòng Mới (Quy Tắc 1 Cửa/Phòng Kín)

Tất cả 4 loại cửa được thiết kế **Cửa Đôi (Double Doors)** tự động lắp vừa ô tường:
- 🚪 **Cửa Gỗ Vàng Văn Phòng (`yellow_wood_door`)**: Cửa gỗ vàng có ô kính quan sát, tay cầm gỉ sét.
- 🌫️ **Cửa Kính Văn Phòng (`office_glass_door`)**: Khung nhôm mờ xám, mặt kính vết nứt sọc chéo.
- ⚙️ **Cửa Lưới Thép Thông Gió (`vent_metal_door`)**: Cửa kim loại công nghiệp phủ vết gỉ cam và lá chắn gió.
- 🌿 **Cửa Rêu Phủ Cổ Kính (`mossy_forest_door`)**: Cửa gỗ mục tối màu phủ rêu xanh dại.

🔒 **Quy tắc 1 cửa duy nhất**: Mỗi căn phòng kín (`isClosedRoom`) chỉ có **đúng 1 cửa vào duy nhất** tại 1 vị trí mặt tường ngẫu nhiên, 3 mặt tường còn lại kín hoàn toàn, giúp tăng độ bí ẩn khi thám hiểm từng căn phòng.

---

## 💀 4. Các Căn Phòng Bẫy Nguy Hiểm

1. **Bẫy Thảm Sụt Lún (`ghost_moist_carpet`)**: Khối thảm ngụy trang không có va chạm (`noCollission`). Bước lên sẽ rớt ngã xuống hố bên dưới.
2. **Bẫy Đèn Chập Điện (`flickering_light`)**: Đèn huỳnh quang chớp tắt phóng tia spark giật mất máu nhẹ và gây choáng (`Nausea`).
3. **Bẫy Phòng Khóa Sập Khói Độc**: Đi vào mở rương bẫy làm cửa sập khóa tự động và xả khói mù mờ (`Blindness`).

---

## 🧪 5. Danh Sách Vật Phẩm Sinh Tồn & Cốt Truyện

- 🥛 **Chai Nước Hạnh Nhân (`almond_water`)**: Uống giúp hồi 4 no, hồi máu và xóa toàn bộ hiệu ứng tiêu cực (Choáng, Mù, Yếu...).
- 🔦 **Đèn Pin Thám Hiểm (`flashlight`)**: Dụng cụ chiếu sáng cầm tay độ bền 256.
- 📻 **Máy Phát Cassette (`cassette_player`)**: Đặt đĩa băng cassette vào nhấp chuột phải để nghe âm thanh ký ức.
- 📼 **Băng Cassette #01, #02, #03 (`cassette_tape_1..3`)**: Tìm thấy trong các ngôi nhà làng bỏ hoang chứa nhật ký giọng nói.
- 🪧 **Biển Cảnh Báo Backrooms (`backrooms_sign`)**: Bảng chỉ dẫn dán tường (`EXIT?`, `DONT LOOK BACK`).

---

## 🐾 6. Quy Tắc Sinh Sản Mob & Cơ Chế Sanity/Fog

- 🚫 **Cấm 100% Hostile Mobs**: Triệt tiêu hoàn toàn quái vật hung dữ (Zombie, Creeper, Enderman, Spider...).
- 🐖 **Động Vật Hiền Lành Xuất Hiện Hiếm**: Dơi (`Bat`), Lợn (`Pig`), Bò (`Cow`), Cừu (`Sheep`) lâu lâu xuất hiện với tỉ lệ 5% trong khu vực rừng tối hoặc đại sảnh.
- 🌫️ **Sương Mù & Âm Thanh Sanity**: Sương mù màu vàng nhạt ma mị (`BackroomsFogEvents`) cùng âm thanh thì thầm hang động vang vọng khi ở quá lâu trong bóng tối (`SanitySystemEvents`).

---

## 👨‍💻 7. Cấu Trúc Mã Nguồn Java (Developer Reference)

Các file chính thuộc package `com.yourname.backrooms`:
- **[BackroomsMod.java](file:///d:/Users/MinhPhuc00ne/Projects/noname/src/main/java/com/yourname/backrooms/BackroomsMod.java)**: Entrypoint chính của Mod, đăng ký EventBus, DeferredRegisters.
- **[ModBlocks.java](file:///d:/Users/MinhPhuc00ne/Projects/noname/src/main/java/com/yourname/backrooms/block/ModBlocks.java)**: Đăng ký toàn bộ 23+ khối block và cửa.
- **[ModItems.java](file:///d:/Users/MinhPhuc00ne/Projects/noname/src/main/java/com/yourname/backrooms/item/ModItems.java)**: Đăng ký vật phẩm Almond Water, Igniter, Flashlight, Tapes.
- **[BackroomsChunkGenerator.java](file:///d:/Users/MinhPhuc00ne/Projects/noname/src/main/java/com/yourname/backrooms/world/BackroomsChunkGenerator.java)**: Thuật toán 3D sinh thế giới v2 quy mô 15x15 cell, trần cao 4..15 block, đại sảnh 15m, siêu rừng tối trần Y=80, khu làng 1-5 nhà độc lập loot khác nhau và quy tắc 1 cửa/phòng kín.
- **[BackroomsPortalBlock.java](file:///d:/Users/MinhPhuc00ne/Projects/noname/src/main/java/com/yourname/backrooms/block/BackroomsPortalBlock.java)**: Xử lý dịch chuyển chiều không gian qua lại giữa Overworld và Level 0.
- **[MobSpawningEvents.java](file:///d:/Users/MinhPhuc00ne/Projects/noname/src/main/java/com/yourname/backrooms/event/MobSpawningEvents.java)**: Kiểm soát tỷ lệ sinh sản mob.
- **[SanitySystemEvents.java](file:///d:/Users/MinhPhuc00ne/Projects/noname/src/main/java/com/yourname/backrooms/client/SanitySystemEvents.java)** & **[BackroomsFogEvents.java](file:///d:/Users/MinhPhuc00ne/Projects/noname/src/main/java/com/yourname/backrooms/client/BackroomsFogEvents.java)**: Xử lý hiệu ứng hình ảnh và âm thanh phía Client.

