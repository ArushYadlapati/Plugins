package dev.frozenmilk.libs

import dev.frozenmilk.FTC
import dev.frozenmilk.easyautolibraries.EasyAutoDependency
import dev.frozenmilk.easyautolibraries.EasyAutoScope

@Suppress("unused", "PropertyName")
class Dairy(ftc: FTC) : EasyAutoScope<Dairy>(ftc) {

    //
    // Util
    //

    val Util by dependency {
        EasyAutoDependency(
            group = "dev.frozenmilk.dairy",
            artifact = "Util",
            version = "1.2.2",
        )
    }

    val UtilUnit by dependency {
        EasyAutoDependency(
            group = "dev.frozenmilk.dairy",
            artifact = "Util-Unit",
            version = "1.1.0",
        )
    }

    //
    // Sinister
    //

    val slothVersion = "0.3.1"

    val Sinister by dependency {
        EasyAutoDependency(
            group = "dev.frozenmilk",
            artifact = "Sinister",
            version = "2.3.0",
        )
    }

    val Sloth by dependency {
        EasyAutoDependency(
            group = "dev.frozenmilk.sinister",
            artifact = "Sloth",
            defaultVersion = { slothVersion },
        )
    }

    val slothboard by dependency {
        EasyAutoDependency(
            group = "com.acmerobotics.slothboard",
            artifact = "dashboard",
            defaultVersion = { "$slothVersion+0.6.0" },
        ) {
            incompatibleWith(
                dependency = ftc.acmerobotics.dashboard,
                reason = "slothboard is a drop-in replacement for dashboard",
            )
        }
    }

    val ftControl = FtControl(ftc) { version, name ->
        EasyAutoDependency(
            group = "com.bylazar.sloth",
            artifact = name,
            defaultVersion = { "0.2.4.1+$version" },
        ) {
            fun incompatibleWithFtControl(dependency: EasyAutoDependency) {
                incompatibleWith(
                    dependency = dependency,
                    reason = "sloth versions of panels libraries are incompatible with non-sloth versions",
                )
            }
            incompatibleWithFtControl(ftc.ftControl.panels)
            incompatibleWithFtControl(ftc.ftControl.battery)
            incompatibleWithFtControl(ftc.ftControl.camerastream)
            incompatibleWithFtControl(ftc.ftControl.capture)
            incompatibleWithFtControl(ftc.ftControl.configurables)
            incompatibleWithFtControl(ftc.ftControl.field)
            incompatibleWithFtControl(ftc.ftControl.gamepad)
            incompatibleWithFtControl(ftc.ftControl.graph)
            incompatibleWithFtControl(ftc.ftControl.lights)
            incompatibleWithFtControl(ftc.ftControl.limelightproxy)
            incompatibleWithFtControl(ftc.ftControl.opmodecontrol)
            incompatibleWithFtControl(ftc.ftControl.pinger)
            incompatibleWithFtControl(ftc.ftControl.telemetry)
            incompatibleWithFtControl(ftc.ftControl.themes)
            incompatibleWithFtControl(ftc.ftControl.utils)
            incompatibleWithFtControl(ftc.ftControl.fullpanels)

        }
    }

    //
    // Mercurial
    //

    val Mercurial by dependency {
        EasyAutoDependency(
            group = "dev.frozenmilk.dairy",
            artifact = "Mercurial",
            version = "2.0.0-beta8",
        )
    }

    val MercurialFTC by dependency {
        EasyAutoDependency(
            group = "dev.frozenmilk.dairy",
            artifact = "MercurialFTC",
            version = "2.0.0-beta9",
        )
    }
}