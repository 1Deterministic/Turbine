package Turbine;

// Obstáculos do mapa
public class Obstaculo extends Objeto {
    private Ponto local; // posição no espaço
    private Forma forma; // modelo 3d do obstáculo
    private Ponto direcao; // direção de movimento do obstáculo
    private Double velocidade; // velocidade do obstáculo
    private Colisor colisor; // container de colisão do obstáculo
    
    // construtor padrão
    public Obstaculo() {
        this.local = new Ponto(0d, 0d, 0d);
        this.direcao = new Ponto(0d, 0d, 0d);
        this.velocidade = 0d;
        this.forma = new Cubo();
        this.colisor = new Colisor();
    }
    
    // costrutor completo
    public Obstaculo(Ponto local, Forma forma, Ponto direcao, Double velocidade, Colisor colisor) {
        this.local = local;
        this.forma = forma;
        this.direcao = direcao;
        this.velocidade = velocidade;
        this.colisor = colisor;
    }
    
    // desenha esse obstáculo (repassa para a forma desenhar)
    public void desenhar(OGL ogl) {
        this.forma.desenhar(ogl);
    }
    
    // define o local do obstáculo (também atualiza o local da forma e colisor atribuídos)
    public void setLocal(Ponto p) {
        this.local = p;
        this.atualizarForma();
        this.atualizarColisor();
    }
    
    // retorna o local do obstáculo
    public Ponto getLocal() {
        return this.local;
    }
    
    // define a forma do obstáculo
    public void setForma(Forma forma) {
        this.forma = forma;
    }
    
    // retorna  a forma do obstáculo
    public Forma getForma() {
        return this.forma;
    }
    
    // define o colisor do obstáculo
    public void setColisor(Colisor c) {
        this.colisor = c;
    }
    
    // retorna o colisor do obstáculo
    public Colisor getColisor() {
        return this.colisor;
    }
    
    // define a direção do obstáculo
    public void setDirecao(Ponto d) {
        this.direcao = d;
    }
    
    // retorna a direção do obstáculo
    public Ponto getDirecao() {
        return this.direcao;
    }
    
    // define a velocidade do obstáculo
    public void setVelocidade(Double v) {
        this.velocidade = v;
    }
    
    // retorna a velocidade do obstáculo
    public Double getVelocidade() {
        return this.velocidade;
    }
    
    // movimenta o obstáculo de acordo com os valores de delta, movimenta também a forma e o colisor anexos
    public void transladar(Ponto delta) {
        this.local.somar(delta);
        this.atualizarForma();
        this.atualizarColisor();
    }
    
    // atualiza o local da forma anexa de acordo com o local do obstáculo
    public void atualizarForma() {
        this.forma.setLocal(new Ponto(this.local));
    }
    
    // atualiza o local do colisor anexo de acordo com o local do obstáculo
    public void atualizarColisor() {
        this.colisor.setLocal(new Ponto(this.local));
    }
    
    // retorna o local apropriado para a câmera
    public Ponto getLocalCamera() {
        return new Ponto(this.local.x, this.local.y, this.local.z + this.forma.getDimensoes().z + ((Math.sqrt(Math.pow(this.forma.getDimensoes().x, 2)) + Math.sqrt(Math.pow(this.forma.getDimensoes().y, 2))) / 2));
    }
    
    // movimenta o obstáculo de acordo com a direção e a velocidade atuais
    public void manterInercia(Double timeDelta) {
        this.transladar(this.direcao.escalar(this.velocidade * timeDelta));
    }
    
    public void aplicarGravidade(Double aceleracao, Double timeDelta) {
        this.direcao.y -= aceleracao * timeDelta;
    }
    
    // aplica as entradas do controle para o obstáculo
    public void movimentar(Controle c, Double timeDelta){
        if (c.direita)
            this.direcao.x += 1d * timeDelta;
        
        if (c.esquerda)
            this.direcao.x -= 1d * timeDelta;
        
        if (c.cima)
            this.direcao.y += 1d * timeDelta;
        
        if (c.baixo)
            this.direcao.y -= 1d * timeDelta;
        
        // limitação de movimento
        if (this.direcao.x > 10d * timeDelta)
            this.direcao.x = 10d * timeDelta;
        else if (this.direcao.x < -10d * timeDelta)
            this.direcao.x = -10d * timeDelta;
        
        if (this.direcao.y > 10d * timeDelta)
            this.direcao.y = 10d * timeDelta;
        else if (this.direcao.y < -10d * timeDelta)
            this.direcao.y = -10d * timeDelta;
    }
    
    // restringe a movimentação a um cubo entre pontoInicial e pontoFinal
    public void limitarAreaMovimento(Ponto pontoInicial, Ponto pontoFinal) {
        if (this.local.x < pontoInicial.x) {
            this.local.x = pontoInicial.x;
            this.direcao.x = 0d;
        } else if (this.local.x > pontoFinal.x) {
            this.local.x = pontoFinal.x;
            this.direcao.x = 0d;
        }
        
        if (this.local.y < pontoInicial.y) {
            this.local.y = pontoInicial.y;
            this.direcao.y = 0d;
        } else if (this.local.y > pontoFinal.y) {
            this.local.y = pontoFinal.y;
            this.direcao.y = 0d;
        }
        
        // ignorar o z por enquanto
    }
}
