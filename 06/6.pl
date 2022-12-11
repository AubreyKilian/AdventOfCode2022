#!/usr/bin/perl -w

use strict;
use warnings;
use File::Basename;
use Data::Dumper;

print "Hello World\n";
my $dirname = dirname(__FILE__);
my $infile = "in";
# $infile = "sample";

# mjqjpqmgbljsphdztnvjfqwrcgsmlb
#    ^^^^
# 123456789x123456789x123456789x

# isSomething("qmgbljsphdztnv",14);
# exit;
open(IN, "<$dirname/$infile") || die "Can't open file\nError: $!";
my $data = <IN>;
# $data = "mjqjpqmgbljsphdztnvjfqwrcgsmlb";    # 7/19 
# $data = "bvwbjplbgvbhsrlpgdmjqwftvncz";      # 23
# $data = "nppdvjthqldpwncqszvftbrmjlhg";      # 23
# $data = "nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg"; # 29
# $data = "zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw";  # 26

my $cnt=0;
my $part1;
my $part2;
foreach (0..length($data)-4) {
    $cnt++;
    my $last4 = substr($data,$_,4);
    my $last14 = substr($data,$_,14);
    if(!defined($part1)) { $part1=$cnt+3 if isSomething($last4,4) == 1; }
    if(!defined($part2)) { $part2=$cnt+13 if isSomething($last14,14) == 1; }
    # $part2=$cnt if isSomething($last14,14) == 1;
    last if defined($part1) && defined($part2);
}

print "$part1\n";
print "$part2\n";



sub isSomething {
    my $in = shift;
    my $num = shift;
    my %uniq;
    my @moo = grep !$uniq{$_}++, split //, $in;
    # print "$in: " . scalar @moo . "\n";
    return scalar @moo == $num?1:0;
}