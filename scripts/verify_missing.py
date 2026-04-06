import json
import os

# File paths
page1_path = r'C:\Users\USER\.gemini\antigravity\brain\da40f0c6-b291-4fc3-a6d9-d48898279097\.system_generated\steps\434\output.txt'
page2_path = r'C:\Users\USER\.gemini\antigravity\brain\da40f0c6-b291-4fc3-a6d9-d48898279097\.system_generated\steps\437\output.txt'
archived_dir = r'C:\Users\USER\Documents\Code\xdm-ex\docs\issues_7.2.11'

with open(page1_path, 'r', encoding='utf-8') as f:
    page1 = json.load(f)
with open(page2_path, 'r', encoding='utf-8') as f:
    page2 = json.load(f)

all_items = page1['items'] + page2['items']

archived_files = os.listdir(archived_dir)
archived_numbers = set()
for f in archived_files:
    if f.startswith('issue_') and f.endswith('.md'):
        try:
            num = int(f.replace('issue_', '').replace('.md', ''))
            archived_numbers.add(num)
        except:
            pass

print(f"Total entries in search: {len(all_items)}")
print(f"Total archived files in directory: {len(archived_numbers)}")

missing = []
for i, item in enumerate(all_items):
    if item['number'] not in archived_numbers:
        missing.append((i+1, item['number']))

print(f"Missing items count: {len(missing)}")
for idx, num in missing:
    print(f"{idx},{num}")
