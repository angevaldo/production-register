package br.com.aegro.production.config;

import br.com.aegro.production.domain.entities.Farm;
import br.com.aegro.production.domain.entities.Field;
import br.com.aegro.production.domain.entities.Production;
import br.com.aegro.production.services.FarmService;
import br.com.aegro.production.services.FieldService;
import br.com.aegro.production.services.ProductionService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Random;

@Configuration
@Profile("test")
public class TestConfig implements CommandLineRunner {

    @Autowired
    private FarmService farmService;

    @Autowired
    private FieldService fieldService;

    @Autowired
    private ProductionService productionService;

    @SneakyThrows
    @Override
    public void run(String... args) throws Exception {
        //farmService.deleteAllFarms();

        int nFarms = 2;//(new Random().nextInt(11) + 40) * 10; // 400 to 500 Farms
        for (int i=0; i < nFarms; i++) {
            Farm farm = new Farm(null, "Farm " + i);
            farm = farmService.create(farm);

            int nFields = 2;//(new Random().nextInt(41) + 10); // 10 to 50 Fields per Farm
            for (int k=0; k < nFields; k++) {
                double area = new Random().nextDouble() * 100d + 10d;
                Field field = new Field(null, "Field " + k, area);
                farm.getFields().add(field);
                field = fieldService.create(field, farm.getId());

                int nProductions = 2;//(new Random().nextInt(21) + 10); // 10 to 30 Productions per Field
                for (int n=0; n < nProductions; n++) {
                    double value = new Random().nextDouble() * 100d + 10d;
                    Production production = new Production(null, value, farm, field);
                    productionService.create(production);
                }
            }
        }
    }

}
