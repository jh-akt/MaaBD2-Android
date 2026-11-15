import os
from pathlib import Path
import runpy
import tempfile
import unittest
from unittest.mock import patch

from prepare_upstream import OVERLAY


class AndroidEntryTest(unittest.TestCase):
    def test_save_directory_survives_payload_generation_changes(self):
        configure = runpy.run_path(str(OVERLAY / "android_entry.py"))["configure_environment"]
        with tempfile.TemporaryDirectory() as temporary, patch.dict(os.environ):
            base = Path(temporary)
            shared = base / "config"
            shared.mkdir()
            generations = [base / "generation-1", base / "generation-2"]
            for generation in generations:
                generation.mkdir()
                (generation / "config").symlink_to(shared, target_is_directory=True)
            first = configure(generations[0])
            (first / "account.json").write_text('{"account": 7}')
            second = configure(generations[1])
            self.assertEqual(first, second)
            self.assertEqual((second / "account.json").read_text(), '{"account": 7}')
            self.assertEqual(os.environ["MFABD2_DATA_DIR"], str(shared / "MFABD2"))
            self.assertEqual(os.environ["MAAFW_BINARY_PATH"], str(generations[1] / "maafw"))


if __name__ == "__main__":
    unittest.main()
