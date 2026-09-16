import glob, re
from collections import defaultdict

all_books = []
for idx in range(1, 8):
    fname = f"app/src/main/java/com/example/data/repository/InitialDataSeedPart{idx}.kt"
    with open(fname, "r", encoding="utf-8") as f:
        txt = f.read()
    pos = 0
    while True:
        b_idx = txt.find("BookEntity(", pos)
        if b_idx == -1:
            break
        start = b_idx
        p_count = 0
        i = start + len("BookEntity")
        while i < len(txt):
            if txt[i] == '(':
                p_count += 1
            elif txt[i] == ')':
                p_count -= 1
                if p_count == 0:
                    break
            i += 1
        end = i + 1
        book_str = txt[start:end]
        all_books.append(book_str)
        pos = end

def extract_field(b, field):
    m = re.search(field + r'\s*=\s*"([^"]*)"', b)
    if m:
        return m.group(1)
    m = re.search(field + r'\s*=\s*([0-9]+)', b)
    if m:
        return m.group(1)
    return ""

class8_books = []
for b in all_books:
    darja = extract_field(b, "darja")
    if "ثامنہ" in darja or "حدیث" in darja:
        title = extract_field(b, "title")
        author = extract_field(b, "author")
        subject = extract_field(b, "subject")
        book_type = extract_field(b, "type")
        class8_books.append({
            "title": title,
            "author": author,
            "subject": subject,
            "type": book_type,
            "darja": darja
        })

print(f"Total Class 8 books in DB: {len(class8_books)}")

with open("class8_report.txt", "w", encoding="utf-8") as out:
    for b in class8_books:
        out.write(f"{b['title']} | {b['author']} | {b['type']} | {b['subject']}\n")

print("Generated report in class8_report.txt")
