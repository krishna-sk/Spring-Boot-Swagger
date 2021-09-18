package com.krishna.data;
import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.krishna.entity.User;
import com.krishna.model.UserModel;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

	private final String[] FIELD_NAMES = new String[] { "id", "first_name", "last_name", "email", "role"};

	@Autowired
	private DataSource dataSource;

	@Autowired
	JobBuilderFactory jobBuilderFactory;

	@Autowired
	JobCompletionNotificationListener listener;
	
	@Autowired
	StepBuilderFactory stepBuilderFactory;
	
	@Bean
    public FlatFileItemReader<UserModel> reader() {
        return new FlatFileItemReaderBuilder<UserModel>().name("UserModelItemReader")
                .resource(new ClassPathResource("Records.csv")).linesToSkip(1)
                .delimited().names(FIELD_NAMES)
                .fieldSetMapper(new BeanWrapperFieldSetMapper<UserModel>() {
                    {
                        setTargetType(UserModel.class);
                    }
                }).build();
    }
	
	@Bean
	public UserItemProcessor processor() {
		return new UserItemProcessor();
	}
	
	
	@Bean
    public JdbcBatchItemWriter<User> writer() {
        return new JdbcBatchItemWriterBuilder<User>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<User>())
                .sql("INSERT INTO user(id,first_name,last_name,email,role) "
                		+ "values(:id,:firstName,:lastName,:email,:role)")
                .dataSource(dataSource).build();
    }
 
	@Bean
	public Job importUserJob() {

		return jobBuilderFactory.get("user-data-insert-job").incrementer(new RunIdIncrementer()).
				listener(listener).flow(step1()).end().build();
	}
	
	

	@Bean
	public Step step1() {

		return stepBuilderFactory.get("insert-user-step").<UserModel, User>chunk(100).reader(reader())
				.processor(processor()).writer(writer()).build();
	}

}