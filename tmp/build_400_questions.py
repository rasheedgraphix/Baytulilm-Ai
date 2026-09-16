# -*- coding: utf-8 -*-
"""
Generates 400 unique, authentic Dars-e-Nizami MCQs (10 questions x 40 chapters)
for the 4-step curriculum:
Step 1 (Beginner): Darja 1 & 2 (10 chapters x 10 qs = 100 qs)
Step 2 (Medium): Darja 3 & 4 (10 chapters x 10 qs = 100 qs)
Step 3 (Advanced): Darja 5 & 6 (10 chapters x 10 qs = 100 qs)
Step 4 (Expert): Darja 7 & 8 (10 chapters x 10 qs = 100 qs)
"""
import re

# We will build all 400 questions systematically
print("Building 400 questions generator...")
