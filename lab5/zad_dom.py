A = {
    "a": "x := x + y",
    "b": "y := y + 2z",
    "c": "x := 3x + z",
    "d": "z := y - z"
}
w = "baadcb"

B = {
    "a": "x := x + 1",
    "b": "y := y + 2z",
    "c": "x := 3x + z",
    "d": "w := w + v",
    "e": "z := y − z",
    "f": "v = x + v"
}
w2 = "acdcfbbe"


def interpreter(A: dict[str, str], w: str) -> (set, set, list, list):
    translated = {}
    for key, val in A.items():
        # [key, dependencies]
        main_id = val[:1]
        dep = []
        for digit in val[2:]:
            if digit.isalpha():
                dep.append(digit)
        translated[key] = [main_id, dep]
    D = set()
    I = set()
    for key1, val1 in translated.items():
        for key2, val2 in translated.items():
            if val1[0] in val2[1] or val2[0] in val1[1] or key1 == key2:
                D.add((key1, key2))
            else:
                I.add((key1, key2))

    FNF = [[]]
    for idx, letter in enumerate(w):
        flag = False
        for i in range(len(FNF) - 1, -2, -1):
            if i == -1:
                FNF[0].append((letter, idx))
                break
            for el, _ in FNF[i]:
                if (letter, el) in D:
                    if i == len(FNF) - 1:
                        FNF.append([(letter, idx)])
                    else:
                        FNF[i + 1].append((letter, idx))
                    flag = True
                    break
            if flag:
                break

    G = [[] for _ in range(len(w))]
    # w branches przechowywane sa poszczegolne galezie zaleznosci
    branches = []
    # lecimy po wszystkich literach ze slowa
    for idx, letter in enumerate(w):
        # potrzebujemy przetrzymywac flage, czy dodalismy cos w trakcie, bo jesli nie dodajemy nowy korzen
        added = False
        for branch in branches.copy():
            # jesli jest zalezny od ostatniego elementu w galezi, po prostu dodaj go na koniec
            if (branch[-1][0], letter) in D:
                # przy dodaniu do branches, od razu zapisaujemy zaleznosci w grafie
                G[branch[-1][1]].append(idx)
                added = True
                branch.append((letter, idx))
                continue
            # w przeciwnym wypadku, przegladamy po wczesniejszych literach w tej galezi i jesli jest zalezne,
            # tworzymy nowa odnoge w branches
            for i in range(len(branch) - 2, -1, -1):
                if (branch[i][0], letter) in D:
                    G[branch[-i][1]].append(idx)
                    added = True
                    branches.append([(letter, idx)])
                    break
        # tutaj dodajemy jesli nie bylo zalezne od niczego wczesniej
        if not added:
            branches.append([(letter, idx)])

    return D, I, FNF, G


def show_results_for(tab, word):
    D, I, fnf, G = interpreter(tab, word)

    formatted_fnf = ""
    for tab in fnf:
        formatted_fnf += "("
        for el in tab:
            formatted_fnf += el[0]
        formatted_fnf += ")"

    print(f"D = {D}")
    print(f"I = {I}")
    print(f"FNF = {formatted_fnf}")
    print("\nGraph (format .dot)")
    print("digraph G {")
    for fr, to_tab in enumerate(G):
        for to in to_tab:
            print(f"  {fr} -> {to};")
    for id, letter in enumerate(word):
        print(f"  {id}[label=\"{letter}\"];")
    print("}")


print("Results for test data 1:")
show_results_for(A, w)

print("\n------------------------")
print("\nResults for test data 2:")
show_results_for(B, w2)
