import concurrent.futures

INPUT_FILE = "in.txt"
NUM_THREADS = 4  # Liczba wątków w puli


class GaussianEliminationFoata:
    def __init__(self, dim, matrix):
        print(f"dim: {dim}")
        print(f"matrix: {matrix}")
        self.N = dim

        self.M = matrix # (Nx(N+1))

        self.m = {} # mnożniki

        self.n = {}


    def task_A(self, k, i):
        """
        Znalezienie mnożnika dla wiersza i, do odejmowania go od k-tego wiersza,
        m[k][i] = M[k][i] / M[i][i]
        """
        multiplier = self.M[k][i] / self.M[i][i]
        self.m[(k, i)] = multiplier
        # print(f"A_{k},{i} -> {multiplier}")

    def task_B(self, k, j, i):
        """
        Pomnożenie j-tego elementu wiersza i przez mnożnik - do odejmowania od k-tego wiersza,
        n[k][j][i] = M[i][j] * m[k][i]
        """
        multiplier = self.m[(k, i)]
        val = self.M[i][j] * multiplier
        self.n[(k, j, i)] = val
        # print(f"B_{k},{j},{i} -> {val}")

    def task_C(self, k, j, i):
        """
        Odjęcie j-tego elementu wiersza i od wiersza k,
        M[k][j] = M[k][j] - n[k][j][i]
        """
        val_to_subtract = self.n[(k, j, i)]
        self.M[k][j] -= val_to_subtract
        # print(f"C_{k},{j},{i} done.")


    def run(self):
        """
        Główna pętla wykonująca eliminację zgodnie z klasami Foaty.
        Kolejność: Dla każdego pivota 'i':
          1. Wykonaj równolegle wszystkie A (Klasa Fa)
          2. Wykonaj równolegle wszystkie B (Klasa Fb)
          3. Wykonaj równolegle wszystkie C (Klasa Fc)
        """
        with concurrent.futures.ThreadPoolExecutor(max_workers=NUM_THREADS) as executor:

            for i in range(self.N - 1):

                # Uruchamiamy zadania A dla wierszy poniżej pivota (k > i)
                futures_A = []
                for k in range(i + 1, self.N):
                    futures_A.append(executor.submit(self.task_A, k, i))

                concurrent.futures.wait(futures_A)


                # Uruchamiamy zadania B dla każdego elementu wiersza k
                futures_B = []
                for k in range(i + 1, self.N):
                    for j in range(i, self.N + 1):
                        futures_B.append(executor.submit(self.task_B, k, j, i))

                concurrent.futures.wait(futures_B)


                # Uruchamiamy zadania C dla kwadratu poniżej pivota
                futures_C = []
                for k in range(i + 1, self.N):
                    for j in range(i, self.N + 1):
                        futures_C.append(executor.submit(self.task_C, k, j, i))

                concurrent.futures.wait(futures_C)

    def backward_substitution(self):
        x = [0.0] * self.N

        # Iterujemy od ostatniego wiersza do góry
        for i in range(self.N - 1, -1, -1):
            sum_ax = sum(self.M[i][j] * x[j] for j in range(i + 1, self.N))
            x[i] = (self.M[i][self.N] - sum_ax) / self.M[i][i]

        return x

    def print_matrix(self):
        print(f"{self.N}")
        for row in self.M:
            print(" ".join(f"{val:3.1f}" for val in row))


def load_matrix(filename):
    try:
        with open(filename, 'r') as f:
            content = f.read().split(sep="\n")
    except FileNotFoundError:
        raise ValueError(f"Error: Nie znaleziono pliku {filename}.")

    dim = int(content[0])
    matrix: list[list] = []
    for idx, line in enumerate(content[1:-1]):
        values_in_line = line.split(sep=" ")
        matrix.append([])
        for value in values_in_line:
            if value != "":
                matrix[idx].append(float(value))
    wyrazy_wolne = content[-1].split(sep=" ")
    for idx in range(dim):
        matrix[idx].append(float(wyrazy_wolne[idx]))
    return dim, matrix



if __name__ == "__main__":
    filename = input("Input filename (default in.txt): ")

    if filename == "":
        dim, matrix = load_matrix(INPUT_FILE)
    else:
        dim, matrix = load_matrix(filename)

    print(f"Obliczenia dla macierzy {dim}x{dim}...")

    solver = GaussianEliminationFoata(dim, matrix)


    solver.run()

    print("\nMacierz trójkątna):")
    solver.print_matrix()

    result = solver.backward_substitution()

    print("\nWynik (wektor x):")
    print(" ".join(f"{x:.4f}" for x in result))