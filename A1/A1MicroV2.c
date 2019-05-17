#include <stdio.h>
#include <stdlib.h>
#include <string.h>

typedef struct item {
    //int price;
    double price;
    char name[20];
    int full;
} item;

int main() {

    int shelves; //Making Size
    int slots; //

    int shelf;  // Choosing
    int slot;  // USER INPUT

 //   int usedShelf[];
  //  int usedSlot[];

    char buffer[50];

   double pri;

   // char* weird = "%[^\n]%*c";        // why? to try and scan for float



   // item *shelf = malloc(sizeof(struct item));

    printf("How many Shelves? : ");
    fgets(buffer,10,stdin);  // output must be an address
    shelves = atoi(buffer);

    printf("How many Slots? : ");
    fgets(buffer,10,stdin);  // output must be an address
    slots = atoi(buffer);

    struct item inventory[shelves][slots];

    do {
        //test used shelf


        //test


        printf("\nAdd Item to your inventory!\nWhen done leave shelf or slot field empty to exit & view inventory\n");

        printf("\nChoose Shelf: ");
        fgets(buffer, 10, stdin);
        shelf = atoi(buffer);

        // test
        //usedShelf[1] =


        if (shelf > shelves) {
            do{
                printf("That is too big, you only have %d shelves. ",shelves);
                fgets(buffer,10,stdin);
                shelf = atoi(buffer);
            }while(shelf > shelves);
        }

        printf("Choose Slot: ");
        fgets(buffer, 10, stdin);
        slot = atoi(buffer);

        if (slot > slots) {
            do{
                printf("That is too big, you only have %d slots. ",slots);
                fgets(buffer,10,stdin);
                slot = atoi(buffer);
            }while(slot > slots);
        }

        printf("\nItem Name: ");
        fgets(inventory[shelf][slot].name, 20, stdin);

        printf("Item Price: ");
        fgets(buffer, 10, stdin);     //works with int and atoi
       // scanf("%lf",&pri);
        //fgets(inventory[shelf][slot].price, 20, stdin);

       // price = fgetc(stdin);
        //scanf("%f", &inventory[shelf][slot].price);       //

       // inventory[shelf][slot].price = atoi(buffer);     // WORKS FOR INT
        inventory[shelf][slot].price = atof(buffer);
       // inventory[shelf][slot].price = pri;
        inventory[shelf][slot].full = 1;

    }while(shelf != NULL && slot != NULL);

    do {
        printf("\nView items in your inventory!\nWhen done leave shelf or slot field empty to exit.\n");

        printf("\nChoose Shelf: ");
        fgets(buffer, 10, stdin);
        shelf = atoi(buffer);

        if (shelf > shelves) {
            do{
                printf("That is too big, you only have %d shelves. ",shelves);
                fgets(buffer,10,stdin);
                shelf = atoi(buffer);
            }while(shelf > shelves);
        }

        printf("Choose Slot: ");
        fgets(buffer, 10, stdin);
        slot = atoi(buffer);

        if (slot > slots) {
            do{
                printf("That is too big, you only have %d slots. ",slots);
                fgets(buffer,10,stdin);
                slot = atoi(buffer);
            }while(slot > slots);
        }

        printf("\n( %d /",shelf);
        printf(" %d )",slot);
        if (inventory[shelf][slot].full == 1)
        {
            printf("\nItem name : %s", inventory[shelf][slot].name);
          //  printf("Item price : %d\n", inventory[shelf][slot].price);    // FOR INT
            printf("Item price : $%f\n", inventory[shelf][slot].price);     // FOR DBL
        } else {
            printf("\n\n(ERROR)No item has been added to this shelf slot.\n");
        }


    }while (shelf != NULL && slot != NULL);




    return 0;
}