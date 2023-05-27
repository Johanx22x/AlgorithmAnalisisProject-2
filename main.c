#include <stdio.h>

int U(int n) {

  switch (n) {
  case 0:
    return 6;
  case 1:
    return 1;
  case 2:
    return 0;
  }

  return U(n - 1) + U(n - 2) - U(n - 3);
}

int main(void) {
  printf("%d\n", U(8));
  return 0;
}
