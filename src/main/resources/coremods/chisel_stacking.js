var ASMAPI = Java.type('net.minecraftforge.coremod.api.ASMAPI');
var Opcodes = Java.type('org.objectweb.asm.Opcodes');
var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');
var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
var InsnNode = Java.type('org.objectweb.asm.tree.InsnNode');

var HOOKS = 'com/qbits/dora/item/ChiselStacking';
var STACK = 'Lnet/minecraft/world/item/ItemStack;';

var redirects = [
    { srg: 'm_41753_', desc: '()Z', hook: 'isStackable', hookDesc: '(' + STACK + ')Z', args: 1 },
    { srg: 'm_150942_', desc: '(' + STACK + STACK + ')Z', hook: 'isSameItemSameTags', hookDesc: '(' + STACK + STACK + ')Z', args: 2 }
];

function redirect(method, r) {
    method.instructions.clear();
    method.tryCatchBlocks.clear();
    if (method.localVariables != null) {
        method.localVariables.clear();
    }
    for (var i = 0; i < r.args; i++) {
        method.instructions.add(new VarInsnNode(Opcodes.ALOAD, i));
    }
    method.instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, HOOKS, r.hook, r.hookDesc, false));
    method.instructions.add(new InsnNode(Opcodes.IRETURN));
    method.maxStack = r.args;
}

function initializeCoreMod() {
    return {
        'dora_chisel_stacking': {
            'target': {
                'type': 'CLASS',
                'name': 'net.minecraft.world.item.ItemStack'
            },
            'transformer': function (node) {
                for (var i = 0; i < redirects.length; i++) {
                    var r = redirects[i];
                    var name = ASMAPI.mapMethod(r.srg);
                    for (var j = 0; j < node.methods.size(); j++) {
                        var method = node.methods.get(j);
                        if (method.name == name && method.desc == r.desc) {
                            redirect(method, r);
                        }
                    }
                }
                return node;
            }
        }
    };
}
