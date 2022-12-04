#!/usr/bin/perl -w

# List::Compare - where have you been all my life?  https://metacpan.org/pod/List::Compare

use strict;
use warnings;
use File::Basename;
use List::Compare;

print "Hello World\n";
my $dirname = dirname(__FILE__);
my $infile = "in";
# $infile = "sample";

open(IN, "<$dirname/$infile") || die "Can't open file\nError: $!";
my @data = <IN>;

my $part1 = 0;
my $part2 = 0;

foreach (@data) {
    chomp;
    my ($p1,$p2) = split(/,/);
    isInside($p1, $p2) && $part1++;
    overlaps($p1, $p2) && $part2++;
}

print "Part1: $part1\n";
print "Part2: $part2\n";

# There must surely be a better/shorter way to do this...
sub expand {
    my $p = shift;
    my @a;
    my ($p1,$p2) = split(/-/, $p);
    foreach($p1..$p2) { push @a, $_;}
    return \@a; #arrayref needed by List::Compare
}

sub isInside {
    my $lc = List::Compare->new(expand(shift),expand(shift));
    return $lc->is_LsubsetR || $lc->is_RsubsetL;
}

sub overlaps {
    my $lc = List::Compare->new(expand(shift),expand(shift));
    return !$lc->is_LdisjointR;
}