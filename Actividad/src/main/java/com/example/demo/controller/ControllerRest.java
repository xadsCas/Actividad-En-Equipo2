/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.demo.controller;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.example.demo.Model.Usuario;

import org.springframework.ui.Model;


import com.example.demo.repository.UsuarioRepository;
import java.io.FileWriter;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author ernes
 */
@RestController
@RequestMapping("/proyecto")
public class ControllerRest {
    

    @Autowired
    private UsuarioRepository usuarioRepository;

    private static final Pattern PATRON_CORREO = Pattern.compile(
        "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    );
    
     @GetMapping("/numero")
    public List<Integer> Aletorio(){
    
        List<Integer> numeros = new ArrayList<>();
        Random random = new Random();
        
        for(int i=0;i<10;i++){
            numeros.add(random.nextInt(100));
        }
        
        return numeros;
    
    }
    
    @PostMapping("/pares")
    public Map<String, List<Integer>> separarParesImpares(@RequestBody List<Integer> numeros) {
    List<Integer> pares = numeros.stream().filter(n -> n % 2 == 0).collect(Collectors.toList());
    List<Integer> impares = numeros.stream().filter(n -> n % 2 != 0).collect(Collectors.toList());
    return Map.of("pares", pares, "impares", impares);
   
}   
    
    
    @PostMapping("/validacorreo")
public String validarCorreo(@RequestBody String correo) {
        
      boolean esValido = PATRON_CORREO.matcher(correo).matches();
        
       if(!esValido){
       return "Correo Valido";
}else{
           return "Correo Invalido";
       }
       
}
        
        
  @PostMapping("/crearArchivo")
public String crearArchivo(
        @RequestParam String nombre,
        @RequestParam String apellidoPaterno,
        @RequestParam String apellidoMaterno,
        @RequestParam int edad,
        @RequestParam String correo,
        Model model) {

    // Crear el objeto usuario
    Usuario usuario = new Usuario();
    usuario.setNombre(nombre);
    usuario.setApellidoPaterno(apellidoPaterno);
    usuario.setApellidoMaterno(apellidoMaterno);
    usuario.setEdad(edad);
    usuario.setCorreo(correo);

    // Guardar el usuario en la base de datos y obtener el ID generado
    usuario = usuarioRepository.save(usuario);

    // Ruta del archivo (configurar esto como propiedad sería mejor)
    String filePath = "C:\\Users\\ernes\\Desktop\\personas.txt";

    // Crear el archivo y escribir los datos
    try (FileWriter writer = new FileWriter(filePath, true)) { // Modo append
        writer.write(usuario.getId() + "|" +
                usuario.getNombre() + "|" +
                usuario.getApellidoPaterno() + "|" +
                usuario.getApellidoMaterno() + "|" +
                usuario.getEdad() + "|" +
                usuario.getCorreo() + "\n");
    } catch (IOException e) {
        throw new RuntimeException("Error al escribir en el archivo personas.txt", e);
    }

    // Pasar el ID al modelo para la vista
    model.addAttribute("id", usuario.getId());
    return "Usuario con id: "+ usuario.getId() +" Creado con exito" ;
}

}





