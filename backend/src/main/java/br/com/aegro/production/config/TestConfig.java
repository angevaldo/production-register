package br.com.aegro.production.config;

import br.com.aegro.production.domain.entities.Farm;
import br.com.aegro.production.domain.entities.Field;
import br.com.aegro.production.domain.entities.Production;
import br.com.aegro.production.domain.repositories.FarmRepository;
import br.com.aegro.production.domain.repositories.FieldRepository;
import br.com.aegro.production.domain.repositories.ProductionRepository;
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
    private FarmRepository farmRepository;

    @Autowired
    private FieldRepository fieldRepository;

    @Autowired
    private ProductionRepository productionRepository;

    @SneakyThrows
    @Override
    public void run(String... args) {
        //productionRepository.deleteAll();
        //farmRepository.deleteAll();

        int nFarms = 2;//(new Random().nextInt(11) + 40); // 40 to 50 Farms
        for (int i=0; i < nFarms; i++) {
            Farm farm = new Farm(null, "Farm " + i);
            farm = farmRepository.insert(farm);

            int nFields = 2;//(new Random().nextInt(41) + 10); // 10 to 50 Fields per Farm
            for (int k=0; k < nFields; k++) {
                double area = new Random().nextDouble() * 100d + 10d;
                Field field = new Field(null, "Field " + k, area, farm);
                farm.getFields().add(field);
                field = fieldRepository.insert(field);
                farmRepository.save(farm);

                int nProductions = 2;//(new Random().nextInt(21) + 10); // 10 to 30 Productions per Field
                for (int n=0; n < nProductions; n++) {
                    double value = new Random().nextDouble() * 100d + 10d;
                    Production production = new Production(null, value, farm, field);
                    productionRepository.insert(production);
                }
            }
        }
    }

}
