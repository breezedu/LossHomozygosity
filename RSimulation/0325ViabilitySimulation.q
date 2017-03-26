#!/bin/sh
#SBATCH --mail-type=ALL
#SBATCH --mail-user=jeff.du@duke.edu
#SBATCH --CPU=64
#SBATCH --job-name=lmer4_SQ
/opt/apps/R-2.15.2/bin/R 0325ViabilitySimulation.R --vanilla