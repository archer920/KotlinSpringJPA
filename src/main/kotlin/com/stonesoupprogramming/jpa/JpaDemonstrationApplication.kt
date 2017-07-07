package com.stonesoupprogramming.jpa

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import javax.persistence.*

@SpringBootApplication
class JpaDemonstrationApplication

@Configuration
//The next line tells Spring Generate our JPA Repositories
@EnableJpaRepositories(basePackages = arrayOf("com.stonesoupprogramming.jpa"))
class Config

//This class maps to a table in the database
//that will get created for us
@Entity
data class Employee(
       @field: Id @field: GeneratedValue var Id : Long = 0, //Primary Key
       var name : String = "", //Column
       var position : String = "") //Column


//The Implementation for this class is generated
//by Spring Data!
interface EmployeeRepository : JpaRepository<Employee, Long>{

    //Define a custom query using Spring Data
    fun countByNameContainingIgnoringCase(name : String) : Long
}

@Controller
@RequestMapping("/")
class IndexController(@Autowired private val employeeRepository: EmployeeRepository) {

    @RequestMapping(method = arrayOf(RequestMethod.GET))
    fun doGet(model : Model) : String {
        model.apply {
            addAttribute("employee", Employee())
            addAttribute("showName", false)
            addAttribute("employees", employeeRepository.findAll().toList())
        }
        return "index"
    }

    @RequestMapping("/employee_save", method = arrayOf(RequestMethod.POST))
    fun doEmployeeSave(employee: Employee,
                       model : Model) : String {
        employeeRepository.save(employee)
        model.apply {
            addAttribute("employee", Employee())
            addAttribute("showName", false)
            addAttribute("employees", employeeRepository.findAll().toList())
        }
        return "index"
    }

    @RequestMapping("/employee_count", method = arrayOf(RequestMethod.POST))
    fun doEmployeeCount(@RequestParam("name") name : String,
                        model : Model) : String {
        val count = employeeRepository.countByNameContainingIgnoringCase(name)
        model.apply {
            addAttribute("employee", Employee())
            addAttribute("showName", true)
            addAttribute("count", "Number of employees having name $name: $count")
            addAttribute("employees", employeeRepository.findAll().toList())
        }
        return "index"
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(JpaDemonstrationApplication::class.java, *args)
}
