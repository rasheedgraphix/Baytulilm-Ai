# -*- coding: utf-8 -*-
import os, sys

def get_raw_q(step_id, ch_num, q_ur, q_en, ar, corr_ur, corr_en, wrong_ur_list, wrong_en_list, exp_ur, exp_en, b_name, d_ur, d_en, subj, cit):
    wrong_ur_str = ", ".join([f'"{w}"' for w in wrong_ur_list])
    wrong_en_str = ", ".join([f'"{w}"' for w in wrong_en_list])
    return f"""        RawQuestion(
            stepId = "{step_id}", chapterNum = {ch_num},
            questionUr = "{q_ur}",
            questionEn = "{q_en}",
            arabic = "{ar}",
            correctUr = "{corr_ur}",
            correctEn = "{corr_en}",
            wrongUr = listOf({wrong_ur_str}),
            wrongEn = listOf({wrong_en_str}),
            expUr = "{exp_ur}",
            expEn = "{exp_en}",
            bookName = "{b_name}", darjaUrdu = "{d_ur}", darjaEn = "{d_en}", subject = "{subj}", citation = "{cit}"
        )"""

print("Helper defined.")
