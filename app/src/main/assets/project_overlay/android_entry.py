"""Adapt MFABD2's Android agent to the shared host's persistent config directory."""
import os
from pathlib import Path
import runpy


def configure_environment(project_root):
    project_root = Path(project_root).resolve()
    # RuntimePayloadStore links config/ to shared storage across payload generations.
    # Resolve the link so upstream never stores saves in a disposable generation.
    storage = (project_root / "config" / "MFABD2").resolve()
    storage.mkdir(parents=True, exist_ok=True)
    os.environ["MFABD2_DATA_DIR"] = str(storage)
    os.environ["MAAFW_BINARY_PATH"] = str(project_root / "maafw")
    return storage


if __name__ == "__main__":
    root = Path(__file__).resolve().parent.parent
    configure_environment(root)
    runpy.run_path(str(root / "agent/main.py"), run_name="__main__")
