import os
import re

def clean_github_issue(content):
    # Find the title which is usually [Title](...#top)
    match = re.search(r'\[(.*?)\]\(https://github.com/.*?/issues/(\d+)#top\)', content)
    if not match:
        return content # Return as is if title not found
    
    title = match.group(1)
    issue_num = match.group(2)
    start_index = match.start()
    
    # The conversation usually ends before the "Terms", "Privacy" etc. footer
    end_match = re.search(r'\[Terms\]\(https://docs.github.com/site-policy/github-terms/github-terms-of-service\)', content)
    if end_match:
        end_index = end_match.start()
        conversation = content[start_index:end_index]
    else:
        conversation = content[start_index:]
    
    return f"# {title} (#{issue_num})\n\n{conversation}"

base_path = r'C:\Users\USER\.gemini\antigravity\brain\da40f0c6-b291-4fc3-a6d9-d48898279097\.system_generated\steps'
output_dir = r'C:\Users\USER\Documents\Code\xdm-ex\docs\issues_7.2.11'

if not os.path.exists(output_dir):
    os.makedirs(output_dir)

steps = {
    # Batch 1-3
    '1319': '65', '1087': '84', '492': '97', '240': '98', '212': '99',
    '1303': '100', '1301': '104', '330': '105', '404': '106', '456': '107',
    '1248': '131', '389': '132', '103': '133', '474': '134', '372': '135',
    '485': '136', '1151': '137', '541': '138', '1261': '139', '1162': '140',
    '175': '168', '498': '169', '173': '170', '479': '171', '1166': '172',
    '569': '173', '127': '174', '348': '175', '384': '176', '256': '177',
    '247': '200', '764': '201', '470': '202', '184': '203', '417': '204',
    '727': '205', '169': '206', '411': '207', '317': '208', '1076': '209',
    # Batch 4.1-4.3
    '499': '260', '894': '261', '737': '262', '567': '263', '227': '264',
    '939': '265', '847': '266', '243': '267', '735': '268', '272': '269',
    '573': '290', '450': '291', '884': '292', '586': '293', '242': '294',
    '364': '295', '371': '296', '328': '297', '234': '298', '362': '299',
    '1253': '317', '366': '318', '1114': '319', '419': '320', '235': '321',
    '828': '322', '589': '323', '490': '324', '391': '325', '374': '326',
    # Batch 5
    '839': '356', '484': '357', '460': '358', '321': '359', '385': '360',
    '322': '361', '307': '362', '990': '363', '802': '364', '426': '365',
    # Batch 6
    "648": "505", "343": "506", "995": "507", "179": "508", "1134": "509",
    "176": "510", "578": "511", "912": "512", "228": "513", "251": "514",
    "192": "515", "455": "516", "429": "517", "554": "518", "274": "519",
    "993": "520", "520": "521", "570": "522", "159": "523", "259": "524",
    # Batch 7
    "196": "548", "977": "549", "535": "550", "342": "551", "314": "552",
    "1208": "553", "1084": "554", "750": "555", "642": "556", "689": "557",
    "207": "558", "790": "559", "326": "560", "927": "561", "669": "562",
    "672": "563", "252": "564", "1283": "565", "913": "566", "688": "567",
    "748": "568", "714": "569", "198": "570", "514": "571", "1014": "572",
    # Batch 8 (Final)
    "568": "590", "336": "591", "949": "592", "560": "593", "1032": "594",
    "755": "595", "241": "596", "1168": "597", "409": "598", "946": "599",
    "779": "600", "1009": "601", "918": "602", "761": "603", "292": "604",
    "291": "605", "1296": "606", "1260": "607", "79": "608", "439": "609",
    "312": "610", "576": "611", "369": "612", "476": "613", "1242": "614"
}

for issue_num, step in steps.items():
    file_path = os.path.join(base_path, step, 'content.md')
    if os.path.exists(file_path):
        with open(file_path, 'r', encoding='utf-8') as f:
            content = f.read()
        
        cleaned = clean_github_issue(content)
        
        output_path = os.path.join(output_dir, f'issue_{issue_num}.md')
        with open(output_path, 'w', encoding='utf-8') as f:
            f.write(cleaned)
        print(f"Saved cleaned issue {issue_num}")
    else:
        print(f"File not found: {file_path}")
