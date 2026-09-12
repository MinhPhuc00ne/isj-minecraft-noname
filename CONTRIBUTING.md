# 🤝 Hướng Dẫn Đóng Góp (Contributing Guidelines)

Cảm ơn bạn đã quan tâm đến việc đóng góp phát triển cho Mod **Infinity Gauntlet**!

## 🚀 Các Bước Đóng Góp

1. **Fork Repository**: Nhấn nút **Fork** ở góc trên bên phải để tạo bản sao dự án về tài khoản GitHub của bạn.
2. **Clone về máy local**:
   ```bash
   git clone https://github.com/YOUR_USERNAME/isj-minecraft-noname.git
   cd isj-minecraft-noname
   ```
3. **Tạo nhánh mới** cho tính năng hoặc sửa lỗi của bạn:
   ```bash
   git checkout -b feature/awesome-new-infinity-ability
   ```
4. **Viết Code & Kiểm Tra**:
   - Tuân thủ cấu trúc package `com.minhphuc.infinitygauntlet.*`.
   - Đảm bảo dự án biên dịch thành công qua Gradle:
     ```bash
     ./gradlew build
     ```
5. **Commit & Push**:
   ```bash
   git add .
   git commit -m "feat: add new infinity stone ability"
   git push origin feature/awesome-new-infinity-ability
   ```
6. **Mở Pull Request (PR)**:
   - Truy cập Repository gốc và nhấn **New Pull Request**.
   - Mô tả rõ các thay đổi và tính năng mới bạn đã bổ sung.

---

## 🐞 Báo Lỗi & Đề Xuất Tính Năng

Nếu bạn gặp lỗi hoặc có ý tưởng mới về Găng Tay Vô Cực, vui lòng mở một issue tại trang [GitHub Issues](https://github.com/MinhPhuc00ne/isj-minecraft-noname/issues).
