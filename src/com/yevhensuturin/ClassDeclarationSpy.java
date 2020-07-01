package com.yevhensuturin;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClassDeclarationSpy {
    enum ClassMember {CONSTRUCTOR, FIELD, METHOD, CLASS, ALL}

    Class classToInvestigate;

    public ClassDeclarationSpy(Class classToInvestigate) {
        this.classToInvestigate = classToInvestigate;
    }

    public void doClassInvestigation(){
        System.out.format("Class:%n %s%n%n", classToInvestigate.getCanonicalName());
        System.out.format("Modifiers:%n %s%n%n", Modifier.toString(classToInvestigate.getModifiers()));
        System.out.println("Type parameters: \n");

        TypeVariable[] typeVariables = classToInvestigate.getTypeParameters();
        if(typeVariables.length == 0){
            System.out.println();
        } else {
            for(TypeVariable typeVariable: typeVariables){
                System.out.println("\t"+typeVariable.getName());
            }
            System.out.println("\n");
        }

        System.out.println("Implemented interfaces:");
        Type[] interfaces = classToInvestigate.getInterfaces();
        if(interfaces.length != 0) {
            for (Type type : interfaces) {
                System.out.println("\t"+type.toString());
            }
        }

        System.out.println("\nInheritance path:");
        List<Class> list = new ArrayList<>();
        printAncestor(classToInvestigate, list);
        list.forEach((cl)->{
            System.out.println("\t"+cl.getCanonicalName());
        });

        System.out.println("\nAnnotations:");
        Annotation[] annotations = classToInvestigate.getAnnotations();
        for(Annotation annotation:annotations){
            System.out.println(annotation.toString());
        }
    }

    public void doMemberInvestigation(ClassMember memberType){
        if(memberType==ClassMember.CONSTRUCTOR){
            printMember(memberType, classToInvestigate, "\t");
        } else {
            printInheritedMembers(memberType, classToInvestigate, "\t");
        }
    }

    private void printInheritedMembers(ClassMember member, Class cl, String pref){
        printMember(member, cl, pref);
        List<Class> classList = new ArrayList<>(Arrays.asList(cl.getInterfaces()));
        Class<?> ancestor = cl.getSuperclass();
        if(ancestor != null) {
            classList.add(ancestor);
        }
        for (Class cl1: classList){
            printInheritedMembers(member, cl1, pref+"\t");
        }
    }

    private void printMember(ClassMember member, Class cl, String pref){
        switch(member){
            case CONSTRUCTOR:
                System.out.println(pref + "Constructors for class" + cl.getCanonicalName());
                Arrays.asList(cl.getConstructors()).forEach((con)->{
                    System.out.println(pref + ((Constructor) con).toGenericString());
                });
                break;
            case FIELD:
                System.out.println(pref + "Fields for class" + cl.getCanonicalName());
                Arrays.asList(cl.getDeclaredFields()).forEach((fld)->{
                    System.out.println(pref + ((Field) fld).toGenericString());
                });
                break;
            case METHOD:
                System.out.println(pref + "Methods for class" + cl.getCanonicalName());
                Arrays.asList(cl.getDeclaredMethods()).forEach((method)->{
                    System.out.println(pref + ((Method) method).toGenericString());
                });
                break;
        }
    }

    public void printAncestor(Class cl, List<Class> list){
        Class<?> ancestor = cl.getSuperclass();
        while(ancestor != null){
            list.add(ancestor);
            ancestor = ancestor.getSuperclass();
        }
    }

}
