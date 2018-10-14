#include <stdio.h>
#include <stdlib.h>
#include <time.h>

#include <pbc.h>
#include <pbc_test.h>
#include <pbc_poly.h>
#include "./pbc/misc/darray.c"
#include "./pbc/arith/fp.c"
#include "./pbc/arith/poly.c"

#define SIZEOFELEMENT 3

void element_generate(pairing_t pairing, field_t fx, element_t *poly_x) {
        for (int i = 0; i < SIZEOFELEMENT; i++) {
                element_init(poly_x[i], fx);
                poly_alloc(poly_x[i], 1);
                element_ptr constcoeff;
				// 返回poly_x[i]的第0个系数
                constcoeff = (element_ptr)poly_coeff(poly_x[i], 0);
                // element_random(constcoeff); // Randomly generate
                element_set_si(constcoeff, i); // Set ith element as i
        }
}

void poly_set_monic(element_ptr f, int deg) {
  int i;
  poly_alloc(f, deg + 1);
  for (i=0; i<deg; i++) {
    element_set0(poly_coeff(f, i));
  }
  element_set1(poly_coeff(f, i));
}


int main(int argc, char **argv) {

        pairing_t pairing;
        field_t fx;

        element_t g;
        element_t secret_key;

        element_t poly_x[SIZEOFELEMENT];
        element_t acc;

        pbc_demo_pairing_init(pairing, argc, argv);

        field_init_fp(pairing->Zr, pairing->r);
        field_init_poly(fx, pairing->Zr);

        element_init(secret_key, fx);
        element_init_G1(g, pairing);


        element_init_G1(acc, pairing);

		// poly_x[i]  就是聚合值的集合
        printf("\nGenerating elements...\n");
        element_generate(pairing, fx, poly_x);
        for (int i = 0; i < SIZEOFELEMENT; i++) {
                element_printf("%B ", poly_x[i]);
        }
        printf("\n");

		//generate x
        printf("\nGenerating s...\n");
        int polyX_degree = 1;
        do {
				// 定义secret_key的polyX_degree次的多项式
                poly_set_monic(secret_key, polyX_degree);
        } while (!poly_is_irred(secret_key));
        element_printf("%B\n", secret_key);


		//generate (x+0)(x+1)...
        element_t xi_plus_s;
        element_t x0_mul_to_xn;

        element_init(xi_plus_s, fx);
        element_init(x0_mul_to_xn, fx);

        poly_set0(xi_plus_s);
        poly_set1(x0_mul_to_xn);

        // Calculate x0_mul_to_xn = (x0+s)(x1+s)...(xn+s)
        for (int i = 0; i < SIZEOFELEMENT; i++) {
                poly_add(xi_plus_s, poly_x[i], secret_key);
                // element_printf("add = %B\n", xi_plus_s);
                poly_mul(x0_mul_to_xn, xi_plus_s, x0_mul_to_xn);
                // element_printf("mul = %B\n", x0_mul_to_xn);
        }

        element_pow_zn(acc, g, x0_mul_to_xn);

        element_printf("mul = %B\n", x0_mul_to_xn);
}