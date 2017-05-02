#!/bin/sh
#SBATCH --mail-type=ALL
#SBATCH --mail-user=jeff.du@duke.edu
#SBATCH --nodes=8
#SBATCH -c 8
#SBATCH --mem=64G
#SBATCH --job-name=425Sim

export PATH=/opt/apps/R-2.15.2/bin:$PATH

R CMD BATCH 0425SimulationPower.R 