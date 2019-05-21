package com.lambdaschool.school.controller;

import com.lambdaschool.school.model.ErrorDetail;
import com.lambdaschool.school.model.Student;
import com.lambdaschool.school.service.StudentService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.data.web.PageableDefault;


import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController
{
    @Autowired
    private StudentService studentService;

    // Please note there is no way to add students to course yet!

    @ApiOperation(value = "return a list of Students, supports pagination", response = Student.class, responseContainer = "List")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query", value = "specifies the page that you want to access"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", value = "specifies the page size"),
            @ApiImplicitParam(name = "sort", dataType = "string", allowMultiple = true, paramType = "query", value = "Sorts result [name, address, etc]")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "all students listed found", response = Student.class),
            @ApiResponse(code = 404, message = "no students found", response = ErrorDetail.class)
    })
    @GetMapping(value = "/students", produces = {"application/json"})
    public ResponseEntity<?> listAllStudents(@PageableDefault(page = 0, size = 2) Pageable pageable) {

        List<Student> myStudents = studentService.findAll(pageable);

        return new ResponseEntity<>(myStudents, HttpStatus.OK);
    }

    @ApiOperation(value = "returns a single Student by id", response = Student.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Student exists", response = Student.class),
            @ApiResponse(code = 404, message = "Student not found", response = ErrorDetail.class)
    })
    @GetMapping(value = "/Student/{StudentId}", produces = {"application/json"})
    public ResponseEntity<?> getStudentById(@ApiParam(value = "restaurant id", required = true, example = "1") @PathVariable Long StudentId) {

        Student r = studentService.findStudentById(StudentId);

        return new ResponseEntity<>(r, HttpStatus.OK);
    }

    @ApiOperation(value = "returns students with matching and similar names, supports pagination", response = Student.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query", value = "specifies the page that you want to access"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", value = "specifies the page size"),
            @ApiImplicitParam(name = "sort", dataType = "string", allowMultiple = true, paramType = "query", value = "Sorts result [name, address, etc]")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "students found", response = Student.class),
            @ApiResponse(code = 404, message = "no student with that name found", response = ErrorDetail.class)
    })
    @GetMapping(value = "/student/namelike/{name}", produces = {"application/json"})
    public ResponseEntity<?> getStudentByNameContaining(@PageableDefault(page = 0, size = 2) Pageable pageable, @PathVariable String name) {

        List<Student> myStudents = studentService.findStudentByNameLike(name, pageable);

        return new ResponseEntity<>(myStudents, HttpStatus.OK);
    }


    @ApiOperation(value = "adds a new student to the database", response = Student.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "student successfully added", response = Student.class),
            @ApiResponse(code = 500, message = "failed to add student", response = ErrorDetail.class)
    })
    @PostMapping(value = "/Student", consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<?> addNewStudent(@Valid @RequestBody Student newStudent) throws URISyntaxException {

        newStudent = studentService.save(newStudent);

        // set the location header for the newly created resource
        HttpHeaders responseHeaders = new HttpHeaders();

        URI newStudentURI = ServletUriComponentsBuilder.fromCurrentRequest().path("/{Studentid}").buildAndExpand(newStudent.getStudid()).toUri();

        responseHeaders.setLocation(newStudentURI);

        return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
    }


    @ApiOperation(value = "updates a student on the database", response = Student.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "student successfully updated", response = Student.class),
            @ApiResponse(code = 500, message = "failed to update student", response = ErrorDetail.class)
    })
    @PutMapping(value = "/Student/{Studentid}")
    public ResponseEntity<?> updateStudent(@RequestBody Student updateStudent, @PathVariable long Studentid) {

        studentService.update(updateStudent, Studentid);

        return new ResponseEntity<>(HttpStatus.OK);
    }


    @ApiOperation(value = "deletes a student from the database", response = Student.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "student successfully deleted", response = Student.class),
            @ApiResponse(code = 500, message = "failed to delete student", response = ErrorDetail.class)
    })
    @DeleteMapping("/Student/{Studentid}")
    public ResponseEntity<?> deleteStudentById(@PathVariable long Studentid) {

        studentService.delete(Studentid);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
