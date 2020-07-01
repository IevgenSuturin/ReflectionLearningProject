package com.yevhensuturin;

import java.io.InterruptedIOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.Identity;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ConcurrentNavigableMap;

public class Main {
    enum E {A,B};

    class InnerClass{
        private int a;

        public InnerClass(int a) {
            this.a = a;
        }

        public InnerClass(){
            a=10;
        }
    }

    public static void main(String[] args) throws ClassNotFoundException {
/*
        List<Class> listClass = new ArrayList<>();
        listClass.add("foo".getClass());
        //listClass.add(System.console());
        listClass.add(E.A.getClass());
        listClass.add(new byte[1024].getClass());
        listClass.add(new HashSet<String>().getClass());

//        Doesn't work
//        boolean b;
//        Class c = b.getClass();
        listClass.add(boolean.class);
        listClass.forEach(System.out::println);

        System.out.println(listClass.get(1).getName());
        System.out.println(InnerClass.class.getName());

        Class innerClass = Class.forName(InnerClass.class.getName());
*/
//        new ClassDeclarationSpy(ConcurrentNavigableMap.class).doClassInvestigation();
//        new ClassDeclarationSpy(String.class).doClassInvestigation();
//        new ClassDeclarationSpy(InterruptedIOException.class).doClassInvestigation();
//        new ClassDeclarationSpy(Identity.class).doClassInvestigation();

//        ClassDeclarationSpy hsSpy = new ClassDeclarationSpy(new HashSet<String>().getClass());
//        hsSpy.doMemberInvestigation(ClassDeclarationSpy.ClassMember.CONSTRUCTOR);
//        hsSpy.doMemberInvestigation(ClassDeclarationSpy.ClassMember.FIELD);
//        hsSpy.doMemberInvestigation(ClassDeclarationSpy.ClassMember.METHOD);


//        Creating new object using *.class file section, invoke some method of the objects the classes
//        using loaded annotation class.
//        You need to copy ReflectedHorse.class ReflectedHorseRace.class SpecialTrack.class files
//        from https://github.com/IevgenSuturin/CyclicBarierEcxample_HorseRace.git project
//        to your class path directory


        Class<?> horseClass = Class.forName("com.yevhensuturin.ReflectedHorse");
        Class<?> horseRaceClass = Class.forName("com.yevhensuturin.ReflectedHorseRace");
        Class<? extends Annotation> trackAnnotationClass = Class.forName("com.yevhensuturin.SpecialTrack").asSubclass(Annotation.class);

        List<Object> reflectedHorses = new ArrayList<>();


         try {
             Constructor horseClassConstructor = horseClass.getConstructor();
             for(int i=0; i<7; i++) {
                 horseClassConstructor.setAccessible(true);
                 Object horse = horseClassConstructor.newInstance();
                 reflectedHorses.add(horse);
             }

//             Constructor horseRaceClassConstructor = horseRaceClass.getConstructor(new Class[]{List.class, int.class});
//             horseRaceClassConstructor.newInstance(new Object[]{reflectedHorses, 200});

        } catch (NoSuchMethodException | IllegalArgumentException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            System.out.println(e.getMessage() + " " + e.getClass());
        }

         reflectedHorses.forEach((horse) ->{
             try {
                 Method toStr = horseClass.getDeclaredMethod("printWithParameters", new Class[]{int.class});

                 Method track = horseClass.getDeclaredMethod("tracks");
                 Annotation annotation = track.getAnnotation(trackAnnotationClass);
                 Method annotationMethod = trackAnnotationClass.getMethod("value");

                 System.out.println("Annotation: " + annotation + " : " + annotationMethod.invoke(annotation));

                 System.out.println("horse.toString() invocation: " + toStr.invoke(horse, 1));
             }catch(NoSuchMethodException | IllegalAccessException | InvocationTargetException  e){
                 System.out.println("Error " + e.getMessage() + " stack " + e.getClass());
             }
         });





//        ClassDeclarationSpy strSpy = new ClassDeclarationSpy(horseClass);
//        strSpy.doMemberInvestigation(ClassDeclarationSpy.ClassMember.CONSTRUCTOR);
//        strSpy.doMemberInvestigation(ClassDeclarationSpy.ClassMember.FIELD);
//        strSpy.doMemberInvestigation(ClassDeclarationSpy.ClassMember.METHOD);

    }
}