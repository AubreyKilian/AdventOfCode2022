#!/usr/bin/perl -w

# I really should polish up on my slightly more intermediate Perl...  I'm rather rusty...

use strict;
use warnings;
use File::Basename;

print "Hello World\n";
my $dirname = dirname(__FILE__);
my $infile = "in.csv";
# $infile = "sample";
open(IN, "<$dirname/$infile") || die "Can't open file\nError: $!";
my @data = <IN>;

# Part 1 - sample input and answer
#my $p1_letters = "pLPvts";
#my $p1_answer = 157;

# Part 2 - sample answers
# first group has "r" (priority=18), second group has "Z" (priority=52)

##### Part 1
my $allcommons = "";
foreach (@data) {
    my $eachlen=length($_)/2;
    my $l = substr($_,0,$eachlen);
    my $r = substr($_,$eachlen);
    $allcommons .= getcommons($l, $r);
}
my $totalpriority = calcAllPriorities($allcommons);

print "part 1: $totalpriority\n";

##### Part 2

my $part2commons = "";
for (my $i=0; $i<$#data; $i = $i + 3) { # This will just barf if the number of lines in the input file is not divisable by 3
    my $l1 = $data[$i+0];
    my $l2 = $data[$i+1];
    my $l3 = $data[$i+2];
    $part2commons .= getcommons(getcommons($l1, $l2), $l3);
}

my $part2totalpriority = calcAllPriorities($part2commons);

print "part 2: $part2totalpriority\n";

sub getcommons {
    my $l = shift; chomp($l);
    my $r = shift; chomp($r);
    my $c = "";
    foreach my $o (split '', $l) {
        if(index($r, $o) > -1 && index($c, $o) == -1){ # only add it once
            $c .= $o;
        }
    }
    return $c;
}

sub calcAllPriorities {
    my $tot = 0;
    foreach my $o (split '', shift) {
        $tot += priority($o);
    }
    return $tot;
}

# Just work it out with the ascii values
sub priority {
    my $letter = shift;
    my $priority = 0;
    my $adjustment = 0;
    if($letter eq lc($letter)) { # if it's a lowercase
        $adjustment = 96;
    } else { # assume it's an upper
        $adjustment = 38; # magicks!
    }
    return ord($letter) - $adjustment; # A = 65, so priority becomes 65-38.  a=97, so priority becomes 97-96.
}

#exit;
# "unit tests"
print priority("a") . "\n"; # should be 1
print priority("A") . "\n"; # should be 27
print getcommons("abc", "cde") . "\n"; # should be "c"
print getcommons("aabbcc", "ccddee") . "\n"; # should be "c"
