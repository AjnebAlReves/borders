plugins {
    `java-script`
    `paper-script`
    `mod-properties-script`
    `shadow-script`
    `modrinth-script`
}

group = "net.axay"
version = "1.3.0"

description = "At any time, the size of the world border is equal to your level count"

modrinth {
    uploadFile.set(tasks.jar.get())
}
