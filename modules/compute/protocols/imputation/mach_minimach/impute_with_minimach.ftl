#OUTPUTS ${preparedStudyDir}/chr${chr}.results
#EXES minimacBin
#LOGS log
#TARGETS project,chr

#FOREACH project,chr

inputs "${vcfReference}.${chr}.vcf.gz"
inputs "${preparedStudyDir}/chr${chr}.gz"
inputs "${preparedStudyDir}/chr${chr}.dat"
alloutputsexist "${preparedStudyDir}/chr${chr}.results"

#Minimac requires a file listing markers in the haplotype file. This file can be easily 
#generated by extracting the second column from the .dat file. 

cut -f 2 -d " " ${preparedStudyDir}/chr${chr}.dat > ${preparedStudyDir}/chr${chr}.snps

#Example
#cut -f 2 -d " " examples/sample.dat > examples/sample.snps

#The imputation step
#TODO: Add ${minimacBin} to parameters.csv
#TODO: Add ${vcfReference}  (/target/gpfs2/gcc/resources/ImputationReferenceSets/GIANT.phase1_release_v3.20101123.snps_indels_svs.genotypes.refpanel.ALL/chr10.phase1_release_v3.20101123.snps_indels_svs.genotypes.refpanel.ALL.vcf.gz)
${minimacBin} --vcfReference --refHaps ${vcfReference}.${chr}.vcf.gz --haps ${preparedStudyDir}/chr${chr}.gz --snps ${preparedStudyDir}/chr${chr}.snps --rounds 5 --states 200 --prefix ${preparedStudyDir}/chr${chr}.results

#Example:
#./minimac --vcfReference --refHaps /target/gpfs2/gcc/resources/ImputationReferenceSets/GIANT.phase1_release_v3.20101123.snps_indels_svs.genotypes.refpanel.ALL/chr10.phase1_release_v3.20101123.snps_indels_svs.genotypes.refpanel.ALL.vcf.gz --haps mach1.out.gz --snps examples/sample.snps --rounds 5 --states 200 --prefix results
