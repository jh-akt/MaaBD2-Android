# MaaBD2 Android

默认中文沟通。本项目通过共享 MaaFramework-Android 宿主运行 MFABD2 的 Android 资源。

- 包名 `com.maaframework.android.bd2`；Android 11+、ARM64、Root。
- UI、主题、ViewModel、任务排序、预览、更新与本地覆盖使用共享框架，不在应用内复制实现。
- 通用修复先提交同级 MaaFramework-Android，再更新框架子模块。
- `upstream.lock.json` 固定上游 Android APK、SHA-256、ABI 和运行时版本。
- `tools/prepare_upstream.py` 只导入上游资源与运行时，不导入上游 Android 宿主。
- `app/src/main/assets/project/`、`runtime/` 是生成目录，不提交、不手工修改。
- 本地适配放在 `app/src/main/assets/project_overlay/`，构建与在线更新共用一份覆盖。
- Agent 的存档放在框架持久化 `config/` 下，不得写入会被替换的资源快照。
- 不提交密钥、机器绝对路径、用户配置或原始设备日志；使用 GitHub noreply 身份提交。
- 验证：Python 工具测试、Gradle 构建与单元测试、`tools/verify_payload.py --apk ...`。
- 没有真机验收时，不声称游戏任务已实测通过。用户恢复授权前不进行手机调试。
