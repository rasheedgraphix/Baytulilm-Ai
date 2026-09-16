# -*- coding: utf-8 -*-
import json
import os

# Build the complete masterQuestionBank with 400 questions (10 questions x 40 chapters)
# Each chapter contains 10 authentic, unique multiple-choice questions with full explanations, Arabic texts, citations, and 4 choices.

def clean_str(s):
    return s.replace('"', '\\"').replace('\n', ' ')

def make_rq(step, ch, q_ur, q_en, ar, c_ur, c_en, w_ur, w_en, exp_ur, exp_en, book, d_ur, d_en, subj, cit):
    w_ur_s = ", ".join(['"' + clean_str(x) + '"' for x in w_ur])
    w_en_s = ", ".join(['"' + clean_str(x) + '"' for x in w_en])
    return f"""        RawQuestion(
            stepId = "{step}", chapterNum = {ch},
            questionUr = "{clean_str(q_ur)}",
            questionEn = "{clean_str(q_en)}",
            arabic = "{clean_str(ar)}",
            correctUr = "{clean_str(c_ur)}",
            correctEn = "{clean_str(c_en)}",
            wrongUr = listOf({w_ur_s}),
            wrongEn = listOf({w_en_s}),
            expUr = "{clean_str(exp_ur)}",
            expEn = "{clean_str(exp_en)}",
            bookName = "{clean_str(book)}", darjaUrdu = "{clean_str(d_ur)}", darjaEn = "{clean_str(d_en)}", subject = "{clean_str(subj)}", citation = "{clean_str(cit)}"
        )"""

print("Helper ready")
