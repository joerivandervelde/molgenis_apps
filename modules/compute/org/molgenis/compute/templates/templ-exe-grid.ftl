#download executable
srmcp -server_mode=passive srm://srm.grid.sara.nl:8443/pnfs/grid.sara.nl/data/lsgrid/${srm_name} \
file:////scratch/${just_name}
chmod 755 /scratch/${just_name}
