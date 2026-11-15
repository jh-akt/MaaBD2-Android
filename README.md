# MaaBD2 Android

使用共享 [MaaFramework-Android](https://github.com/jh-akt/MaaFramework-Android)
宿主运行 [MFABD2](https://github.com/sunyink/MFABD2) 的《棕色尘埃 2》Android 任务资源。
这是独立的社区接入项目，不是 MFABD2 维护者发布或背书的客户端。

## 功能

- 与 MaaFgo、BBB、GF2、MaaEnd 使用相同的首页、任务、设置、日志和主题。
- 支持 ADB 的任务前置；不支持的任务后置并禁止执行。
- 任务选项、预设、手动 Pipeline Override JSON、预览触控由框架处理。
- 默认每 6 小时检查上游正式版 Android 发布包；准备、运行与停止期间延后更新。
- 任务资源、Python Agent 与 MaaFramework 原生库一起更新，并检查运行时兼容性。
- 构建和在线更新均在上游资源上应用 `project_overlay/`，本地适配不会被更新抹掉。
- Agent 存档使用框架的持久化 `config/MFABD2/`，跨资源更新保留。

## 环境

Android 11 或以上、ARM64、Root。游戏语言按上游要求设置为简体中文。
本项目使用共享框架的 Root 运行方式，不继承上游宿主的 Shizuku 实现。
首次使用前请完成游戏登录和数据下载，再从启动任务开始。

## 构建

需要 JDK 21、Android SDK 36、NDK 27.0.12077973、CMake 3.22.1。
SDK 路径使用环境变量或不提交的 `local.properties`。

```bash
git clone --recurse-submodules https://github.com/jh-akt/MaaBD2-Android.git
cd MaaBD2-Android
python3 -m unittest discover -s tools -p 'test_*.py'
./gradlew :app:assembleDebug :app:testDebugUnitTest
python3 tools/verify_payload.py --apk app/build/outputs/apk/debug/app-debug.apk
```

构建自动下载 `upstream.lock.json` 固定的上游 Android APK，校验大小与 SHA-256，
提取任务和匹配的运行时。也可以使用 `python3 tools/prepare_upstream.py --apk 文件路径`
导入已经下载的同版本 APK；同样必须通过校验。
产物为 `app/build/outputs/apk/debug/app-debug.apk`。

## 更新与覆盖

发布源和兼容性检查位于 `app/src/main/assets/maa_project_manifest.json`。
本地适配定义位于 `app/src/main/assets/project_overlay/overlay.json`。
目前覆盖 Agent 入口，让上游识别共享宿主提供的原生库与持久化存档目录。
上游发布格式或 Python/MaaFramework 版本不兼容时保留已有资源，需要更新本宿主适配后再升级。
自动更新针对任务资源与运行时；宿主 APK 自身需要单独安装更新。

`runtime/` 和 `app/src/main/assets/project/` 是生成目录，不提交到 Git。
上游参考源码可另外克隆到同级 `../MFABD2/`，正式构建只依赖锁定的发布包。

## 来源与许可证

本宿主及共享框架使用 AGPL-3.0，详见 `LICENSE`。
MFABD2 资源和 Agent 保留其原有 MIT/Apache-2.0 许可证与第三方声明；
Python、MaaFramework、NumPy、OpenCV 等运行时组件保留各自许可证。
发布包中的许可证随对应资源和运行时一起导入。
应用使用独立图标，上游项目标识及素材权利说明见
[MFABD2 TRADEMARKS.md](https://github.com/sunyink/MFABD2/blob/main/TRADEMARKS.md)。
