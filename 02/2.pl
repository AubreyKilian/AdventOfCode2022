#!/usr/bin/perl -w

use strict;
use warnings;

my $win=6;
my $draw=3;
my $lose=0;

my $rock=1;
my $paper=2;
my $scissors=3;

my %itemscores = (
    "A" => 1,
    "B" => 2,
    "C" => 3,
    "X" => 1, # lose
    "Y" => 2, # draw
    "Z" => 3  # win
);

my %scores = (
    "A" => {
        "X" => $draw,
        "Y" => $win,
        "Z" => $lose
    },
    "B" => {
        "X" => $lose,
        "Y" => $draw,
        "Z" => $win
    },
    "C" => {
        "X" => $win,
        "Y" => $lose,
        "Z" => $draw
    }
);

my %part2moves = (
    "A" => {
        "X" => $lose + $scissors,
        "Y" => $draw + $rock,
        "Z" => $win  + $paper
    },
    "B" => {
        "X" => $lose + $rock,
        "Y" => $draw + $paper,
        "Z" => $win  + $scissors
    },
    "C" => {
        "X" => $lose + $paper,
        "Y" => $draw + $scissors,
        "Z" => $win  + $rock
    }
);


open(IN,"<in");
my @moves=<IN>;

my $myscore=0;
my $p2score=0;
foreach my $r (@moves) {
    chomp($r);
    my ($them, $me) = split(/\s/,$r,2);
    my $thisscore = $scores{$them}{$me};
    #print "($them) + ($me) = $thisscore\n";
    my $itemscore = $itemscores{$me};
    $myscore += $thisscore;
    $myscore += $itemscore;
}

print "$myscore\n";


foreach my $r (@moves) {
    chomp($r);
    my ($them, $me) = split(/\s/,$r,2);
    my $thisscore = $part2moves{$them}{$me};
    #print "($them) + ($me) = $thisscore\n";
    $p2score += $thisscore;
}

print "$p2score\n";
