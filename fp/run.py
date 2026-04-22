import sys
from pathlib import Path

# Add src directory to path so we can import main
sys.path.insert(0, str(Path(__file__).parent / "src"))

from main import main


def run_ga():
    """Run GA on both problems"""
    # Get the project root
    fp_dir = Path(__file__).parent
    project_root = fp_dir.parent
    reports_dir = project_root / "reports"
    
    # Ensure reports directory exists
    reports_dir.mkdir(exist_ok=True)
    
    # Run GA on both problems
    problems = [
        ("onemax", project_root / "problems/onemax.json", reports_dir / "results_onemax_fp.json"),
        ("knapsack", project_root / "problems/knapsack.json", reports_dir / "results_knapsack_fp.json")
    ]
    
    for problem_name, config_file, output_file in problems:
        # print(f"\nRunning GA on {problem_name}...")
        try:
            main(['--config', str(config_file), '--out', str(output_file)])
            # print(f"Results saved to {output_file}")
        except Exception as e:
            print(f"GA execution failed for {problem_name}: {e}")
            return 1
    
    # print("\n" + "=" * 60)
    # print("All tasks completed successfully!")
    # print("=" * 60)
    # print(f"Results and plots: {reports_dir}")
    return 0


if __name__ == "__main__":
    sys.exit(run_ga())