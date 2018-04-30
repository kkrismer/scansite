#!/usr/bin/perl

use warnings;
use strict;
use DBI;

my $DBHOST = "18.159.1.152";
my $DBNAME = "motifs";
my $DBPORT = 3306;
my $DBUSER = "scansite";
my $DBPW = 'enterPassword';

my $DBH = DBI->connect("DBI:mysql:$DBNAME:$DBHOST:$DBPORT", $DBUSER, $DBPW)
  or die ("Error - Connection to database failed: ".DBI->errstr);

my $groupsSql = "SELECT mgroup_id, mgroup_name_short, mgroup_name FROM mgroup";
my $motifsSql = "SELECT motif_id, motif_name_short, motif_name, gene_card, mgroup_id FROM main";
my $matrixSql = "SELECT motif_id, position, score_A, score_B, score_C, score_D, score_E, score_F, score_G, score_H, score_I, score_K, score_L, score_M, score_N, score_P, score_Q, score_R, score_S, score_T, score_U, score_V, score_W, score_X, score_Y, score_Z, score_END, score_START FROM matrix WHERE  motif_id = ? ORDER BY position";
my %map = (2=>'A', 3=>'B', 4=>'C', 5=>'D', 6=>'E', 7=>'F', 8=>'G', 9=>'H', 10=>'I', 11=>'K', 12=>'L', 13=>'M', 14=>'N', 15=>'P', 16=>'Q', 17=>'R', 18=>'S', 19=>'T', 20=>'U', 21=>'V', 22=>'W', 23=>'X', 24=>'Y', 25=>'Z', 26=>'*', 27=>'$') ;
my $start = time();

my %groups = ();

my $sql = $DBH->prepare($groupsSql);
$sql->execute();
my $cnt = 0;
while (my @rows = $sql->fetchrow_array) {
  $groups{$rows[0]}{"SHORT"} = $rows[1];
  $groups{$rows[0]}{"NAME"} = $rows[2];
}

open(MS_FH, ">motifs.txt") or die("Error - Can't open file");
$sql = $DBH->prepare($motifsSql);
$sql->execute();
while (my @rows = $sql->fetchrow_array) {
  print MS_FH "[MOTIF_NAME]\t$rows[2]\n";
  print MS_FH "[MOTIF_SHORT]\t$rows[1]\n";
  print MS_FH "[GENECARD]\t$rows[3]\n";
  print MS_FH "[GROUP_NAME]\t".$groups{$rows[4]}{"NAME"}."\n";
  print MS_FH "[GROUP_SHORT]\t".$groups{$rows[4]}{"SHORT"}."\n";
  print MS_FH "//\n";
  my $mNick = $rows[1];

  my $dataSql = $DBH->prepare($matrixSql);
  $dataSql->execute($rows[0]);
  open(M_FH, ">$mNick.txt") or die("Error - Can't open file");
  my $first = 1;
  for (my $key=2; $key < 28; ++$key) {
    if ($first) {
      $first = 0;
      print M_FH "$map{$key}";
    } else {
      print M_FH "\t$map{$key}";
    }
  }
  print M_FH "\n";
  while (my @rs = $dataSql->fetchrow_array) {
    $first = 1;
    for (my $i = 2; $i < @rs; ++$i) {
      if ($first) {
        $first = 0;
        print M_FH "$rs[$i]";
      } else {
        print M_FH "\t$rs[$i]";
      }
    }
    print M_FH "\n";
  }
  close M_FH;
  $dataSql->finish;
}
$sql->finish;
close MS_FH;
my $end = time();
printf ("time %.1fs\n", $cnt, ($end - $start ) );

