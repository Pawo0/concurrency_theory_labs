#include <stdio.h>
#include <stdlib.h>
#include <math.h>


int SIZE;

#define _first_(i,j) first[ (j)*SIZE + (i) ]
#define _second_(i,j) second[ (j)*SIZE + (i) ]
#define _multiply_(i,j) multiply[ (j)*SIZE + (i) ]

#include <sys/time.h>
#include <time.h>

static double gtod_ref_time_sec = 0.0;

/* Adapted from the bl2_clock() routine in the BLIS library */

double dclock()
{
  double         the_time, norm_sec;
  struct timeval tv;
  gettimeofday( &tv, NULL );
  if ( gtod_ref_time_sec == 0.0 )
    gtod_ref_time_sec = ( double ) tv.tv_sec;
  norm_sec = ( double ) tv.tv_sec - gtod_ref_time_sec;
  the_time = norm_sec + tv.tv_usec * 1.0e-6;
  return the_time;
}


int mm_omp(double * first, double * second, double * multiply)
{
  register unsigned int i,j,k;
  double sum = 0;


  #pragma omp parallel for shared(first, second, multiply, SIZE) private(i, j, k, sum)
  for (i = 0; i < SIZE; i++) { //rows in multiply
    for (j = 0; j < SIZE; j++) { //columns in multiply
      sum = 0;
      for (k = 0; k < SIZE; k++) { //columns in first and rows in second
            sum = sum + _first_(i,k)*_second_(k,j);
          }
      _multiply_(i,j) = sum;
    }
  }
  return 0;
}

int mm_seq(double * first, double * second, double * multiply)
{
  register unsigned int i,j,k;
  double sum = 0;

  for (i = 0; i < SIZE; i++) { //rows in multiply
    for (j = 0; j < SIZE; j++) { //columns in multiply
      sum = 0;
      for (k = 0; k < SIZE; k++) { //columns in first and rows in second
            sum = sum + _first_(i,k)*_second_(k,j);
          }
      _multiply_(i,j) = sum;
    }
  }
  return 0;
}

int verify_results(double * res_seq, double * res_omp) {
    int i;
    int errors = 0;
    for (i = 0; i < SIZE * SIZE; i++) {
        if (fabs(res_seq[i] - res_omp[i]) > 1e-9) {
            errors++;
            if (errors < 5) { 
                 printf("Blad na pozycji %d: Seq=%f, Omp=%f\n", i, res_seq[i], res_omp[i]);
            }
        }
    }
    return errors;
}

int main( int argc, const char* argv[] )
{
  int i,j,iret;
  double ** first;
  double ** second;
  double ** multiply_seq;
  double ** multiply_omp;

  double * first_;
  double * second_;
  double * multiply_seq_;
  double * multiply_omp_;

  double dtime;

  SIZE = 1000;

  first_ = (double*) malloc(SIZE*SIZE*sizeof(double));
  second_ = (double*) malloc(SIZE*SIZE*sizeof(double));
  multiply_seq_ = (double*) malloc(SIZE*SIZE*sizeof(double));
  multiply_omp_ = (double*) malloc(SIZE*SIZE*sizeof(double));

  first = (double**) malloc(SIZE*sizeof(double*));
  second = (double**) malloc(SIZE*sizeof(double*));
  multiply_seq = (double**) malloc(SIZE*sizeof(double*));
  multiply_omp = (double**) malloc(SIZE*sizeof(double*));

  for (i = 0; i < SIZE; i++) {
    first[i] = first_ + i*SIZE;
    second[i] = second_ + i*SIZE;
    multiply_seq[i] = multiply_seq_ + i*SIZE;
    multiply_omp[i] = multiply_omp_ + i*SIZE;
  }

  for (i = 0; i < SIZE; i++) { //rows in first
    for (j = 0; j < SIZE; j++) { //columns in first
      first[i][j]=i+j;
      second[i][j]=i-j;
    }
  }




  double start_time, time_seq, time_omp;
  printf("Obliczanie dla SIZE = %d...\n", SIZE);

  


  start_time = dclock();
  mm_seq(first_, second_, multiply_seq_);
  time_seq = dclock() - start_time;
  printf("Czas Sequential: %le sec\n", time_seq);

  


  start_time = dclock();
  mm_omp(first_, second_, multiply_omp_);
  time_omp = dclock() - start_time;
  printf("Czas OpenMP    : %le sec\n", time_omp);



  printf("Przyspieszenie : %.2fx\n", time_seq / time_omp);




  printf("Porownanie:\n");
  int errors = verify_results(multiply_seq_, multiply_omp_);
  
  if (errors == 0) {
      printf("Wyniki sa sobie rowne\n");
  } else {
      printf("ERROR %d roznic w wynikach.\n", errors);
  }

  fflush( stdout );

  free(first_);
  free(second_);
  free(multiply_seq_);
  free(multiply_omp_);

  free(first);
  free(second);
  free(multiply_seq);
  free(multiply_omp);
  
  return iret;
}
