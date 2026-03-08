#include "sort_bus_lines.h"
#define sort_by0 DISTANCE
#define sort_by1 DURATION


//TODO add implementation here
void bubble_sort (BusLine *start, BusLine *end)
{
    //loop over i placing the max element in the end
    for (int i=0 ; i<(end-start)-1; i++) {
        for (int j = 0 ;j<(end-start)-i-1;j++) {
            //switch if neccary
            if (strcmp((start+j)->name,(start+j+1)->name)>0) {
                Xchange(start+j,start+j+1);


            }
        }
    }
}
void quick_sort (BusLine *start, BusLine *end, SortType sort_type) {
    //split the array recursivly
    if (start<end-1) {
        BusLine* pivot = partition(start,end,sort_type);
        quick_sort (pivot, end, sort_type);
        quick_sort (start, pivot, sort_type);

    }


}




BusLine *partition (BusLine *start, BusLine *end, SortType sort_type) {


    BusLine* i =  start-1;
    BusLine* pivot =  end-1;
    //by distans
    for (BusLine* j = start; j <pivot; j++ ) {
        if (sort_type==DISTANCE) {
            if (j->distance<= pivot->distance) {
                i++;
                Xchange(i,j);}
        }
        //by duration
        else {
            if (j->duration<= pivot->duration) {
                i++;
                Xchange(i,j);}
        }

    }
    Xchange(i+1,pivot);
    return i+1;
}

void Xchange (BusLine *leftp, BusLine *rightp) {
    BusLine temp;
    temp = *leftp;
    *leftp = *rightp;
    *rightp = temp;



}


