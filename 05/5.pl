#!/usr/bin/perl -w

# Beware dear reader, I've made this code rather compressed and concise after I solved it.
# Lots of multi-line instructions compressed into single lines.  Perl can get nasty like that.

use strict;
use warnings;
use File::Basename;

print "Hello World\n";
my $dirname = dirname(__FILE__);
my $infile = "in";
# $infile = "sample";

open(IN, "<$dirname/$infile") || die "Can't open file\nError: $!";
my @data = <IN>;

my $seenstacks=0;
my @initialColumns;
my @moves;

# parse input data into initial column setup and moves list
foreach my $l (@data) {
    chomp($l);
    if($l eq "") {
        $seenstacks=1; # Newline signifies we've seen the initial stack setup, now let's start doing the moves
    }
    if(!$seenstacks) {
        push @initialColumns, $l;
    } else {
        push @moves, $l unless $l eq "";
    }
}

pop @initialColumns; # don't care about that list of column numbers

########## Part 1
my @p1stacks = createStacks(@initialColumns);

# Execute the moves
foreach my $m (@moves) {
    my ($num, $from, $to) = unpackMoveLine($m);
    foreach (1..$num) {
        push @{$p1stacks[$to-1]}, pop @{$p1stacks[$from-1]};
    }
}

########## Part 2
my @p2stacks = createStacks(@initialColumns);

# Execute them moves
foreach my $m (@moves) {
    my ($num, $from, $to) = unpackMoveLine($m);
    push @{$p2stacks[$to-1]}, splice(@{$p2stacks[$from-1]}, -$num);
}

my $part1 = getTops(@p1stacks);
my $part2 = getTops(@p2stacks);

print "$part1\n";
print "$part2\n";

# Create the array of stacks with starting positions
sub createStacks {
    my @initialColumns = @_;
    my @stacks=[];
    foreach my $l (reverse @initialColumns) {
        my @initialColumns = unpack("(A4)*", $l);
        for(my $i=0; $i <= $#initialColumns; $i++) {
            my $item = $initialColumns[$i];
            $item =~ s/\[|\]//g;
            push @{$stacks[$i]}, $item unless $item eq "";
        }
    }
    return @stacks;
}

# Get the top crate from each stack
sub getTops {
    my $retval = "";
    foreach my $n (0..$#_) {
        $retval .= ${$_[$n]}[(scalar @{$_[$n]})-1];
    }
    return $retval;
}

# Expand a move line into its individual parts
sub unpackMoveLine {
    return shift =~ m/^move (\d+) from (\d+) to (\d+)$/;
}