import os
import re

output_dir = r'C:\Users\USER\Documents\Code\xdm-ex\docs\issues_7.2.11'
index_file = os.path.join(output_dir, 'INDEX.md')

issues = []

for filename in os.listdir(output_dir):
    if filename.startswith('issue_') and filename.endswith('.md'):
        file_path = os.path.join(output_dir, filename)
        with open(file_path, 'r', encoding='utf-8') as f:
            first_line = f.readline().strip()
            # Expecting "# Title (#Number)"
            match = re.search(r'# (.*?) \(#(\d+)\)', first_line)
            if match:
                title = match.group(1)
                num = match.group(2)
                issues.append((int(num), title, filename))
            else:
                # Fallback
                num_match = re.search(r'issue_(\d+)\.md', filename)
                if num_match:
                    issues.append((int(num_match.group(1)), first_line.lstrip('# ').strip(), filename))

# Sort by issue number
issues.sort()

with open(index_file, 'w', encoding='utf-8') as f:
    f.write("# XDM 7.2.11 - Archived Open Issues Index\n\n")
    f.write(f"Total Archived Issues: {len(issues)}\n\n")
    f.write("| Issue # | Title | Link |\n")
    f.write("| :--- | :--- | :--- |\n")
    for num, title, filename in issues:
        f.write(f"| {num} | {title} | [{filename}](file:///./{filename}) |\n")

print(f"Index generated with {len(issues)} items.")
