/**
 * For generating rows x cols racks. The back of the rack
 * is along the XY plane and the partitions open along +X axis.
 * 
 * How to use
 * ==========
 * 
 * MAC
 * ---
 * 
 * open -n /Applications/OpenSCAD.app --args -o ~/Documents/work/ShopAssist/Models/rack10x10.stl -D 'rows=10' -D 'cols=10'  -D 'l=100' -D 'h=100' -D 'd=20' -D 't=2' ~/Documents/work/ShopAssist/Model\ Scripts/rack.scad
 * 
 * -D can be used to override any params in this file and hence we can generate racks of any dimension using this
 * same file.
 * -o is used to provide a target file name.
 *
 * @author: Saurabh Saxena
 */

l=100; // Outer Length
h=100; // Outer Height
d=20; // Outer Depth
t=2; // Material thickness
rows=5;
cols=5;

module plank(length, width, thickness) {
	cube([length, width, thickness]);
}

module xy_plank() {
	plank(l, h, t);
}

module yz_plank() {
	plank(t, h, d);
}

module xz_plank() {
	plank(l, t, d);
}

// Back
xy_plank();

// Horizontal planks
for (y_offset = [0 : (h-t)/rows : h-t]) {
	translate([0, y_offset, 0]) {
		xz_plank();
	}
}

// Vertical planks
for (x_offset = [0 : (l-t)/cols : (l-t)]) {
	translate([x_offset, 0, 0]) {
		yz_plank();
	}
}