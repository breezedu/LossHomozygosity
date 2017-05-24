#!/bin/sh
#SBATCH --mail-type=ALL
#SBATCH --mail-user=jeff.du@duke.edu
#SBATCH --nodes=8
#SBATCH -c 4
#SBATCH --mem=32G
#SBATCH --job-name=0517HalfSim

export PATH=/opt/apps/R-2.15.2/bin:$PATH

R CMD BATCH 0517SimulationPower.R 