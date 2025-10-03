import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns
import numpy as np
import os

def load_and_analyze_data():
    """Load CSV data and generate performance plots"""
    csv_file = "benchmark_results.csv"

    if not os.path.exists(csv_file):
        print(f"Error: {csv_file} not found. Run benchmarks first.")
        return

    df = pd.read_csv(csv_file)
    print("Data loaded successfully:")
    print(f"Total records: {len(df)}")
    print(f"Array sizes: {df['ArraySize'].unique()}")
    print(f"Distributions: {df['InputType'].unique()}")

    return df

def create_performance_plots(df):
    """Create various performance analysis plots"""

    # Set style
    plt.style.use('seaborn-v0_8')
    sns.set_palette("husl")

    # 1. Time vs Array Size (log-log plot)
    plt.figure(figsize=(12, 8))

    plt.subplot(2, 2, 1)
    for distribution in df['InputType'].unique():
        subset = df[df['InputType'] == distribution]
        grouped = subset.groupby('ArraySize')['Time(ns)'].mean()
        plt.loglog(grouped.index, grouped.values, 'o-', label=distribution, alpha=0.7)

    plt.xlabel('Array Size')
    plt.ylabel('Time (ns)')
    plt.title('Time Complexity Analysis\n(Log-Log Scale)')
    plt.legend(bbox_to_anchor=(1.05, 1), loc='upper left')
    plt.grid(True, alpha=0.3)

    # 2. Comparisons vs Array Size
    plt.subplot(2, 2, 2)
    for distribution in ['RANDOM', 'SORTED', 'WORST_CASE']:
        subset = df[df['InputType'] == distribution]
        if len(subset) > 0:
            grouped = subset.groupby('ArraySize')['Comparisons'].mean()
            plt.plot(grouped.index, grouped.values, 'o-', label=distribution, alpha=0.7)

    plt.xlabel('Array Size')
    plt.ylabel('Number of Comparisons')
    plt.title('Comparison Count Analysis')
    plt.legend()
    plt.grid(True, alpha=0.3)

    # 3. Array Access vs Array Size
    plt.subplot(2, 2, 3)
    for has_majority in [True, False]:
        subset = df[df['HasMajority'] == has_majority]
        grouped = subset.groupby('ArraySize')['ArrayAccess'].mean()
        plt.plot(grouped.index, grouped.values, 'o-',
                 label=f'Has Majority: {has_majority}', alpha=0.7)

    plt.xlabel('Array Size')
    plt.ylabel('Array Accesses')
    plt.title('Memory Access Patterns')
    plt.legend()
    plt.grid(True, alpha=0.3)

    # 4. Distribution comparison for fixed size
    plt.subplot(2, 2, 4)
    fixed_size = 10000
    subset = df[df['ArraySize'] == fixed_size]
    if len(subset) > 0:
        sns.boxplot(data=subset, x='InputType', y='Time(ns)')
        plt.xticks(rotation=45)
        plt.title(f'Performance Distribution\n(Size = {fixed_size})')
        plt.tight_layout()

    plt.tight_layout()
    plt.savefig('performance_analysis.png', dpi=300, bbox_inches='tight')
    plt.show()

def create_theoretical_validation(df):
    """Compare theoretical vs actual performance"""
    plt.figure(figsize=(10, 6))

    # Theoretical O(n) line
    subset = df[df['InputType'] == 'RANDOM']
    theoretical_n = np.array([n * 100 for n in subset['ArraySize'].unique()])  # Scale factor

    # Actual performance
    actual_times = subset.groupby('ArraySize')['Time(ns)'].mean().values

    plt.plot(subset['ArraySize'].unique(), theoretical_n, 'r--',
             label='Theoretical O(n)', linewidth=2)
    plt.plot(subset['ArraySize'].unique(), actual_times, 'bo-',
             label='Actual Performance', alpha=0.7)

    plt.xlabel('Array Size (n)')
    plt.ylabel('Time (ns)')
    plt.title('Theoretical vs Actual Performance\n(Linear Complexity Validation)')
    plt.legend()
    plt.grid(True, alpha=0.3)
    plt.savefig('theory_vs_practice.png', dpi=300, bbox_inches='tight')
    plt.show()

def generate_statistical_summary(df):
    """Generate statistical analysis of benchmark results"""
    print("\n=== STATISTICAL SUMMARY ===")

    # Basic statistics
    print(f"Total benchmark runs: {len(df)}")
    print(f"Array sizes tested: {sorted(df['ArraySize'].unique())}")

    # Performance by size
    print("\nAverage Time by Array Size:")
    time_by_size = df.groupby('ArraySize')['Time(ns)'].agg(['mean', 'std', 'min', 'max'])
    print(time_by_size.round(2))

    # Performance by distribution
    print("\nAverage Time by Input Distribution:")
    time_by_dist = df.groupby('InputType')['Time(ns)'].agg(['mean', 'std'])
    print(time_by_dist.round(2))

    # Complexity analysis
    print("\nComplexity Analysis (Time vs Size correlation):")
    correlation = df[['ArraySize', 'Time(ns)']].corr().iloc[0,1]
    print(f"Correlation coefficient: {correlation:.4f}")

    # Efficiency metrics
    print("\nEfficiency Metrics:")
    df['TimePerElement'] = df['Time(ns)'] / df['ArraySize']
    efficiency = df.groupby('ArraySize')['TimePerElement'].mean()
    print("Average time per element (ns):")
    print(efficiency.round(2))

if __name__ == "__main__":
    print("Boyer-Moore Majority Vote Performance Analysis")
    print("=" * 50)

    df = load_and_analyze_data()
    if df is not None:
        generate_statistical_summary(df)
        create_performance_plots(df)
        create_theoretical_validation(df)
        print("\nPlots saved as 'performance_analysis.png' and 'theory_vs_practice.png'")