#!/usr/bin/env python3
import subprocess
import os
import sys
from pathlib import Path


def run_command(cmd, cwd=None):
    """Run a shell command and return exit code"""
    # print(f"Running: {cmd}")
    result = subprocess.run(cmd, shell=True, cwd=cwd)
    return result.returncode


def main():
    # Get the project root and oop directory
    oop_dir = Path(__file__).parent
    project_root = oop_dir.parent
    reports_dir = project_root / "reports"
    
    # Ensure reports directory exists
    reports_dir.mkdir(exist_ok=True)
    
    # Step 1: Compile everything
    compile_cmd = (
        "javac -d bin -cp \"lib/*\" "
        "src/models/*.java src/utils/*.java src/fitnesses/*.java "
        "src/strategies/*.java src/core/*.java src/reporter/*.java "
        "src/setup/*.java src/Main.java"
    )
    exit_code = run_command(compile_cmd, cwd=str(oop_dir))
    if exit_code != 0:
        print("Compilation failed!")
        return 1
    # print("Compilation successful!\n")
    
    # Step 2: Run GA on both problems
    problems = [
        ("Onemax", "problems/onemax.json", f"{reports_dir}/results_onemax_oop.json"),
        ("Knapsack", "problems/knapsack.json", f"{reports_dir}/results_knapsack_oop.json")
    ]
    
    ga_outputs = []
    
    for problem_name, config_file, output_file in problems:
        print(f"\nRunning GA (OOP) on {problem_name}...")
        run_cmd = f"java -cp \"oop/bin:oop/lib/*\" Main --config {config_file} --out {output_file}"
        exit_code = run_command(run_cmd, cwd=str(project_root))
        if exit_code != 0:
            print(f"GA execution failed for {problem_name}!")
            return 1
        ga_outputs.append((problem_name, output_file))

    print("\nAll tasks completed successfully!\n")
    return 0

if __name__ == "__main__":
    sys.exit(main())
