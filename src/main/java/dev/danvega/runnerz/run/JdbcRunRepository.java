package dev.danvega.runnerz.run;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;


@Repository
public class JdbcRunRepository{
    private static final Logger log = LoggerFactory.getLogger(JdbcRunRepository.class);
    private final JdbcClient jdbcClient;

    public JdbcRunRepository(JdbcClient jdbcClient){
        this.jdbcClient= jdbcClient;
    }

    public List<Run> findAll(){
        return jdbcClient.sql("select * from Run")
                .query(Run.class)
                .list();
    }

    public Optional<Run> findById(Integer id){
        return jdbcClient.sql("select id,title,started_on, completed_on,miles,location from Run where id =:id")
                .param("id",id)
                .query(Run.class)
                .optional();
    }

    public void create(Run run){
        var updated = jdbcClient.sql("insert into Run (id,title,started_on,completed_on,miles,location) values (?,?,?,?,?,?)")
                .params(List.of(run.id(), run.title(),run.startedOn(),run.completedOn(),run.miles(),run.location().toString()))
                .update();
        Assert.state(updated==1,"Filed to create on "+run.title());
    }

    public void update(Run run,Integer id){
        var updated = jdbcClient.sql("update run set title=?, started_on=?, completed_on =?, miles=?,location=? where id=?")
                .params(List.of(run.title(),run.startedOn(),run.completedOn(),run.miles(),run.location().toString(),id))
                .update();
        Assert.state(updated==1,"Filed to update on "+run.title());
    }
    public void delete(Integer id){
        var updated = jdbcClient.sql("delete from Run where id=:id")
                .param("id",id)
                .update();
        Assert.state(updated==1,"Filed to delete on "+id);
    }

    public int count(){
        return jdbcClient.sql("select * from Run").query().listOfRows().size();
    }

    public void saveAll(List<Run> runs){
        runs.stream().forEach(this::create);
    }
    public List<Run> findByLocation(String location){
        return jdbcClient.sql("select * from Run where location=:location")
                .param("location",location)
                .query(Run.class)
                .list();
    }

//    private List<Run> runs = new ArrayList<>();
//
//    List<Run> findAll(){
//        return runs;
//    }
//
//    Optional<Run> findById(Integer id){
//        return runs.stream()
//                .filter(run->run.id()==id)
//                .findFirst();
//    }
//
//    void create(Run run){
//        runs.add(run);
//    }
//
//    void update(Run run, Integer id){
//        Optional<Run> existingRun = findById(id);
//        if(existingRun.isPresent()){
//            runs.set(runs.indexOf(existingRun.get()),run);
//        }
//    }
//
//    void delete(Integer id){
//        runs.removeIf(run->run.id().equals(id));
//    }
//
//    @PostConstruct
//    private void init(){
//        runs.add(new Run(1,"Monday Morning RUn", LocalDateTime.now(),LocalDateTime.now().plus(30, ChronoUnit.MINUTES),3,Location.OUTDOOR));
//        runs.add(new Run(2,"Wednesday Evening RUn", LocalDateTime.now(),LocalDateTime.now().plus(60, ChronoUnit.MINUTES),6,Location.INDOOR));
//    }
}
