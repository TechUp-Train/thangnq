# 🎓 Jetpack Compose Training

Dự án training Jetpack Compose cho team Apero — 7 buổi, từ cơ bản đến nâng cao.

## 🚀 Cách chạy

1. Mở project bằng **Android Studio Ladybug** (2024.2+)
2. Sync Gradle
3. Chạy app trên emulator/device (minSdk 24)
4. Chọn session từ màn hình chính

## 📁 Cấu trúc project

```
app/src/main/kotlin/com/apero/composetraining/
├── MainActivity.kt          ← Màn hình chọn session
├── common/                  ← Theme, sample data, navigation
│   ├── AppTheme.kt
│   ├── SampleData.kt
│   └── Navigation.kt
├── session1/                ← Compose Fundamentals
│   ├── demos/               ← Code demo (trainer trình bày)
│   └── exercises/           ← Bài tập (học viên làm)
├── session2/                ← Layouts & Lazy Components
├── session3/                ← State & Recomposition
├── session4/                ← Theming & Styling
├── session5/                ← Navigation & Side Effects
├── session6/                ← Animation & Gesture
└── session7/                ← Testing & Performance
```

## 📖 Hướng dẫn từng session

| Session | Chủ đề | Demo | Exercises |
|---------|--------|------|-----------|
| 1 | Compose Fundamentals | Text, Button, Modifier, Column/Row/Box | GreetingCard, ContactCard, ProfileScreen |
| 2 | Layouts & Lazy | LazyColumn, LazyRow, Grid, Scaffold | TodoList, MovieBrowser, PhotoGallery |
| 3 | State & Recomposition | remember, State hoisting, derivedStateOf | Counter, ShoppingCart, SearchFilter |
| 4 | Theming & Styling | MaterialTheme, Dark mode, CompositionLocal | WeatherCard, CustomTheme, DesignSystem |
| 5 | Navigation | NavHost, Arguments, Bottom nav | TwoScreenFlow, TabApp, ECommerceFlow |
| 6 | Animation & Gesture | animate*AsState, AnimatedVisibility, Drag | LikeButton, ExpandableList, SwipeCard |
| 7 | Testing & Performance | ComposeTestRule, @Stable, Performance | CounterTests, LoginTests, PerformanceAudit |

## 🔄 Workflow

### Buổi sáng (Trainer demo)
1. Mở file trong `sessionX/demos/`
2. Chạy `@Preview` để xem kết quả
3. Live code từng concept

### Buổi chiều (Học viên thực hành)
1. Mở file trong `sessionX/exercises/`
2. Tìm comment `// TODO:` và hoàn thành code
3. Chạy `@Preview` để kiểm tra kết quả
4. 3 levels: ⭐ Easy → ⭐⭐ Medium → ⭐⭐⭐ Challenge

## 🛠 Tech Stack

- **Kotlin** 2.0.21
- **Compose BOM** 2025.01.00
- **Material 3**
- **Navigation Compose** 2.8.5
- **Coil** 2.7.0 (image loading)
- **kotlinx-serialization** (type-safe navigation)

## 💡 Tips

- Dùng **Preview** thay vì build app mỗi lần thay đổi
- Đọc comment trong demo code để hiểu concept
- Exercise có gợi ý trong TODO comment — đọc kỹ trước khi code
- Không cần internet — tất cả data đều hardcoded
