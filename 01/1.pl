#!/usr/bin/perl -w

use strict;
use warnings;

open(IN, "<in");
my @a=<IN>;

my %k=();
my $i=0;

foreach my $one (@a) {
    chomp($one);
    if($one eq "") {
        $i++; } 
    else {
        #print "(($one))\n";
        $k{$i}+=$one;
    }
}
my @items=();
foreach my $t (sort keys %k) {
    push @items, $k{$t}+0;
}

# part 1
my @sorteditems = sort { $a <=> $b } @items;
my $top = pop @sorteditems;
print "$top\n";

# part 2
my $part2 = $top + (pop @sorteditems) + (pop @sorteditems);
print "$part2\n";

