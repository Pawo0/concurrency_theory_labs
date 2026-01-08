# Given a string consisting of letters 'a' and 'b',
# find the longest substring where there is equal number of 'a's and 'b's.

def solution(s: str):
    n = len(s)
    global_max = 0
    for i in range(n):
        cnt_a = 0
        cnt_b = 0
        for j in range(i, n):
            if s[j] == "a":
                cnt_a += 1
            else:
                cnt_b += 1
            if cnt_a == cnt_b:
                global_max = max(cnt_a + cnt_b, global_max)
    return global_max


def better_solution(s: str):
    ratio = []
    curr = 0
    for idx, digit in enumerate(s):
        curr += 1 if digit == "b" else -1
        ratio.append((curr, idx))
    low = {}
    high = {}
    for val, idx in ratio:
        if val not in low or idx < low[val]:
            low[val] = idx
        if val not in high or idx > high[val]:
            high[val] = idx
    res = 0
    for val, idx in ratio:
        if val in low:
            res = max(res, abs(idx-low[val]))
        if val in high:
            res = max(res, abs(idx-high[val]))
    return res

